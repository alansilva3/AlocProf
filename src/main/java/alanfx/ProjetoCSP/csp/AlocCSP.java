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
		
		//adicionando restricoes selecionadas pelo usuario
		RestricoesCtrl restricoesCtrl = new RestricoesCtrl(this);
		restricoesCtrl.addRestricoesSelecionadas(restricoesList, disciplinas, variaveis, variaveisUnicas, preferencias); 
		
		restricoesCtrl.addAllUnicoProfessor(disciplinas);
		restricoesCtrl.addAllPriorizarProfessores(variaveis, this.professores, preferencias);
		
		/*
		 * Aqui se insere a restricao para atribuir valores a cada variavel
		 * dinamicamente e gerar os melhores resultados
		 */
		addConstraint(restricaoDinamica); 
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
}