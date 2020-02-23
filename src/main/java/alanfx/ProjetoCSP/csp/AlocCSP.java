package alanfx.ProjetoCSP.csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;
import alanfx.ProjetoCSP.restricoes.HorarioDiferente;
import alanfx.ProjetoCSP.restricoes.HorarioFixo;
import alanfx.ProjetoCSP.restricoes.PreferenciaDisciplina;
import alanfx.ProjetoCSP.restricoes.ProfessorDiferente;
import alanfx.ProjetoCSP.restricoes.util.PriorizarProfessores;
import alanfx.ProjetoCSP.restricoes.util.UnicoProfessor;

public class AlocCSP extends CSP<Variable, List<String>> {

	private List<Variable> variaveis;
	private List<Variable> variaveisUnicas;
	private List<String> professores;
	private Map<String, List<Variable>> preferencias;
	
	public static final List<String> aulas = new ArrayList<>(
		Arrays.asList("SEG17","TER17","QUA17","QUI17","SEX17",
					  "SEG19","TER19","QUA19","QUI19","SEX19",
					  "SEG21","TER21","QUA21","QUI21","SEX21"));

	public AlocCSP(List<Disciplina> disciplinas, 
			 	   List<Professor> professores,
			 	   List<String> restricoesList,
			 	   Constraint<Variable, List<String>> restricaoDinamica) {
		this.variaveis = criarVariaveis(disciplinas);
		addVariaveis(variaveis);
		this.professores = criarProfessores(professores); 
		this.variaveisUnicas = criarVariaveisUnicas(disciplinas);
		
		Domain<List<String>> domain = new Domain<>(createValues(this.professores, aulas));
		for (Variable var : getVariables())
			setDomain(var, domain);
		
		this.preferencias = criarPreferencias(professores);
		
		addRestricoesSelecionadas(restricoesList, disciplinas); //adicionando restricoes selecionados pelo usuario
		
		addAllUnicoProfessor(disciplinas);
		addAllPriorizarProfessores(variaveis, this.professores, preferencias);
		
		/*
		 * Aqui se insere a restricao para atribuir valores a cada variavel
		 * dinamicamente para gerar os melhores resultados
		 */
		addConstraint(restricaoDinamica); 
	}
	
	private void addRestricoesSelecionadas(List<String> restricoesList, List<Disciplina> disciplinas) {
		if(restricoesList.contains("HorarioDiferente")) {
			addAllHorarioDiferente(variaveis, 0); //add "HorarioDiferente"
		}
		if(restricoesList.contains("ProfessorDiferente")) {
			addAllProfessorDiferente(variaveisUnicas, 0); //add "ProfessorDiferente"
		}
		if(restricoesList.contains("PreferenciaDisciplina")) {
			addAllPreferencias(variaveis, preferencias);
		}
		if(restricoesList.contains("HorarioFixo")) {
			addAllHorarioFixo(disciplinas);
		}
	}

	private void addAllHorarioFixo(List<Disciplina> disciplinas) {
		for(Disciplina disc : disciplinas) {
			if(!disc.getHorariosFixos().isEmpty()) {
				for(int i = 0; i< disc.getHorariosFixos().size();i++) {
					addConstraint(new HorarioFixo<>(disc.getVars().get(i), disc.getHorariosFixos().get(i)));
				}
			}
		}
	}

	private void addAllUnicoProfessor(List<Disciplina> disciplinas) {
		for(Disciplina disc : disciplinas) {
			if(disc.getVars().size() > 1) {
				addConstraint(new UnicoProfessor<>(disc.getVars().get(0), disc.getVars().get(1)));
			}
			if(disc.getVars().size() > 2) {
				addConstraint(new UnicoProfessor<>(disc.getVars().get(0), disc.getVars().get(2)));
				addConstraint(new UnicoProfessor<>(disc.getVars().get(1), disc.getVars().get(2)));
			}
		}
	}

	private void addAllPreferencias(List<Variable> variaveis2, Map<String, List<Variable>> preferencias2) {
		for(Variable var : variaveis2) {
			addConstraint(new PreferenciaDisciplina<>(var, preferencias2));
		}
	}

	private List<Variable> criarVariaveisUnicas(List<Disciplina> disciplinas) {
		List<Variable> vars = new ArrayList<>();
		for(Disciplina disc : disciplinas) {
			vars.add(disc.getVars().get(0));
		}
		return vars;
	}

	private Map<String, List<Variable>> criarPreferencias(List<Professor> professores2) {
		Map<String, List<Variable>> prefs = new HashMap<>();
		for(Professor prof : professores2) {
			if(!prof.getPreferencias().isEmpty())
				prefs.put(prof.getNome(), prof.getPreferencias());
		}
		return prefs;
	}

	public static List<String> criarProfessores(List<Professor> professores2) {
		List<String> profs = new ArrayList<>();
		for(Professor prof : professores2) {
			profs.add(prof.getNome());
		}
		profs.add("semProf");
		return profs;
	}

	private void addVariaveis(List<Variable> variaveis2) {
		for(Variable var : variaveis2)
			addVariable(var);
	}

	public static List<Variable> criarVariaveis(List<Disciplina> disciplinas) {
		List<Variable> vars = new ArrayList<>();
		for(Disciplina disc : disciplinas) {
			for(Variable var : disc.getVars()) {
				vars.add(var);
			}
		}
		return vars;
	}

	//Associa Todos os professores a cada um dos horarios
	public static List<List<String>> createValues(List<String> profs, List<String> aulas) {
		List<List<String>> values = new ArrayList<>();
		for(String prof : profs) {
			for(String aula : aulas) {
				values.add(Arrays.asList(aula, prof)); //LIST<AULA, PROFESSOR>
			}
		}
		return values;
	}
	
	//Adiciona todas as restricoes do tipo "HorarioDiferenteConstraint"
	private void addAllHorarioDiferente(List<Variable> var, int j) {
		for(int i = j+1; i < var.size(); i++){
			addConstraint(new HorarioDiferente<>(var.get(j), var.get(i)));
		}
		if(j+1 < var.size()) addAllHorarioDiferente(var, j+1);
	}
	//Adiciona todas as restricoes do tipo "ProfessorDiferenteConstraint"
	private void addAllProfessorDiferente(List<Variable> var, int j) {
		for(int i = j+1; i < var.size(); i++){
			addConstraint(new ProfessorDiferente<>(var.get(j), var.get(i)));
		}
		if(j+1 < var.size()) addAllProfessorDiferente(var, j+1);
	}
	//Adiciona todas as restricoes do tipo "PriorizarProfessores"
	private void addAllPriorizarProfessores(List<Variable> vars, List<String> profs, Map<String, List<Variable>> preferencias) {
		List<String> profs2 = new ArrayList<>(profs);
		profs2.remove("semProf");
		for(Variable var : vars) {
			addConstraint(new PriorizarProfessores<>(var, profs2, preferencias));
		}
	}
}