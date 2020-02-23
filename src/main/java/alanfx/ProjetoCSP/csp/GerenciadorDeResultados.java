package alanfx.ProjetoCSP.csp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;
import alanfx.ProjetoCSP.csp.entidades.BlocoAula;
import alanfx.ProjetoCSP.csp.entidades.Disciplina;
import alanfx.ProjetoCSP.csp.entidades.Horario;
import alanfx.ProjetoCSP.csp.entidades.Professor;

public class GerenciadorDeResultados {

	private final List<Disciplina> disciplinas;
	private final List<Professor> professores;
	private final List<BlocoAula> blocos;
	private Set<Optional<Assignment<Variable, List<String>>>> solucoesList;
	
	public GerenciadorDeResultados(List<Disciplina> disciplinas, List<Professor> professores,
			Set<Optional<Assignment<Variable, List<String>>>> solucoesList) {
		this.disciplinas = disciplinas;
		this.professores = professores;
		this.solucoesList = solucoesList;
		this.blocos = criarBlocos();
	}
	
	public List<Horario> gerarHorarios(){
		List<Horario> horarios = new ArrayList<>();
		if(solucoesList.isEmpty()) return horarios;
		
		for (Optional<Assignment<Variable, List<String>>> soluc : solucoesList) {
			LinkedHashMap<Variable, List<String>> assignment = soluc.get().getVariableToValueMap();
			List<Disciplina> discs = clonarDiciplinas();
			List<Professor> profs = clonarProfessores();
			List<BlocoAula> blocs = cloneBlocos();
			
			//Inserir os horarios alocados em disciplinas e professores
			insereHorarioAlocados(assignment, discs, profs);
			
			//Associar cada professor a uma disciplina
			associarProfDisciplinas(discs, profs);
			
			//Associar cada disciplina a um bloco de aula
			associarDiscBlocos(discs, blocs);
			
			//Criar um horario e adiociona-lo na lista de horarios
			horarios.add(new Horario(blocs));
		}
		return horarios;
	}

	private void insereHorarioAlocados(LinkedHashMap<Variable, List<String>> assignment, List<Disciplina> discs,
			List<Professor> profs) {
		for (Map.Entry<Variable, List<String>> entry : assignment.entrySet()) {
			for(Professor professor : profs) {
				if(professor.getNome().equals(entry.getValue().get(1))) {
					professor.addHorarioAlocado(entry.getValue().get(0));
				}
			}
			for(Disciplina disciplina : discs) {
				if(disciplina.contem(entry.getKey())) {
					disciplina.addHorarioAlocado(entry.getValue().get(0));
				}
			}
		}
	}

	private void associarProfDisciplinas(List<Disciplina> discs, List<Professor> profs) {
		for(Disciplina disc : discs) {
			for(Professor prof : profs) {
				for(String horario : prof.getHorariosAlocados()) {
					if(disc.getHorariosAlocados().contains(horario)) {
						disc.setProfessor(prof);
						break;
					}
				}
			}
		}
	}

	private void associarDiscBlocos(List<Disciplina> discs, List<BlocoAula> blocs) {
		for(BlocoAula bloco : blocs) {
			for(Disciplina disc : discs) {
				if(disc.getHorariosAlocados().contains(bloco.getNome())) {
					bloco.setDisciplina(disc);
					break;
				}
			}
		}
	}

	private List<Professor> clonarProfessores(){
		List<Professor> profsClones = new ArrayList<>();
		for(Professor prof : professores) {
			try {
				profsClones.add(prof.clone());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return profsClones;
	}

	private List<Disciplina> clonarDiciplinas() {
		List<Disciplina> discClones = new ArrayList<>();
		for(Disciplina disc : disciplinas) {
			try {
				discClones.add(disc.clone());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return discClones;
	}
	
	private List<BlocoAula> cloneBlocos() {
		List<BlocoAula> blocClones = new ArrayList<>();
		for(BlocoAula bloc : blocos) {
			try {
				blocClones.add(bloc.clone());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return blocClones;
	}

	private List<BlocoAula> criarBlocos() {
		List<BlocoAula> blocos = new ArrayList<>();
		for(String nome : AlocCSP.aulas) {
			blocos.add(new BlocoAula(nome));
		}
		return blocos;
	}
}
