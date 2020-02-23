package alanfx.ProjetoCSP.csp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.search.csp.Variable;
import alanfx.ProjetoCSP.csp.entidades.Disciplina;
import alanfx.ProjetoCSP.restricoes.HorarioDiferente;
import alanfx.ProjetoCSP.restricoes.HorarioFixo;
import alanfx.ProjetoCSP.restricoes.PreferenciaDisciplina;
import alanfx.ProjetoCSP.restricoes.ProfessorDiferente;
import alanfx.ProjetoCSP.restricoes.util.PriorizarProfessores;
import alanfx.ProjetoCSP.restricoes.util.UnicoProfessor;

public class RestricoesCtrl {

	private AlocCSP csp;
	
	public RestricoesCtrl(AlocCSP csp) {
		this.csp = csp;
	}
	
	public void addRestricoesSelecionadas(List<String> restricoesList, 
			List<Disciplina> disciplinas,
			List<Variable> variaveis,
			List<Variable> variaveisUnicas,
			Map<String, List<Variable>> preferencias) {
		if(restricoesList.contains("HorarioDiferente")) {
			addAllHorarioDiferente(variaveis, 0);
		}
		if(restricoesList.contains("ProfessorDiferente")) {
			addAllProfessorDiferente(variaveisUnicas, 0);
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
					csp.addConstraint(new HorarioFixo<>(disc.getVars().get(i), disc.getHorariosFixos().get(i)));
				}
			}
		}
	}

	private void addAllPreferencias(List<Variable> variaveis2, Map<String, List<Variable>> preferencias2) {
		for(Variable var : variaveis2) {
			csp.addConstraint(new PreferenciaDisciplina<>(var, preferencias2));
		}
	}
	
	//Adiciona todas as restricoes do tipo "HorarioDiferenteConstraint"
	private void addAllHorarioDiferente(List<Variable> var, int j) {
		for(int i = j+1; i < var.size(); i++){
			csp.addConstraint(new HorarioDiferente<>(var.get(j), var.get(i)));
		}
		if(j+1 < var.size()) addAllHorarioDiferente(var, j+1);
	}
	
	//Adiciona todas as restricoes do tipo "ProfessorDiferenteConstraint"
	private void addAllProfessorDiferente(List<Variable> var, int j) {
		for(int i = j+1; i < var.size(); i++){
			csp.addConstraint(new ProfessorDiferente<>(var.get(j), var.get(i)));
		}
		if(j+1 < var.size()) addAllProfessorDiferente(var, j+1);
	}
	
	//Adiciona todas as restricoes do tipo "PriorizarProfessores"
	public void addAllPriorizarProfessores(List<Variable> vars, List<String> profs, Map<String, List<Variable>> preferencias) {
		List<String> profs2 = new ArrayList<>(profs);
		profs2.remove("semProf");
		for(Variable var : vars) {
			csp.addConstraint(new PriorizarProfessores<>(var, profs2, preferencias));
		}
	}
	
	public void addAllUnicoProfessor(List<Disciplina> disciplinas) {
		for(Disciplina disc : disciplinas) {
			if(disc.getVars().size() > 1) {
				csp.addConstraint(new UnicoProfessor<>(disc.getVars().get(0), disc.getVars().get(1)));
			}
			if(disc.getVars().size() > 2) {
				csp.addConstraint(new UnicoProfessor<>(disc.getVars().get(0), disc.getVars().get(2)));
				csp.addConstraint(new UnicoProfessor<>(disc.getVars().get(1), disc.getVars().get(2)));
			}
		}
	}
}
