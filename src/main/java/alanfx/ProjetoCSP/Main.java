package alanfx.projetocsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CspListener;
import aima.core.search.csp.Variable;
import alanfx.projetocsp.csp.AlgoritmoCtrl;
import alanfx.projetocsp.entidades.BlocoAula;
import alanfx.projetocsp.entidades.Disciplina;
import alanfx.projetocsp.entidades.Horario;
import alanfx.projetocsp.entidades.Professor;
import alanfx.projetocsp.utils.GerenciadorDeResultados;
import alanfx.projetocsp.utils.Timer;

public class Main {
	
	private static List<Disciplina> disciplinas = new ArrayList<>();
	private static List<Professor> professores = new ArrayList<>();
	private static List<String> restricoesList;
	
	public static void main(String[] args) {
		
		Disciplina fisica = new Disciplina("Fisica", 4, Arrays.asList());
		Disciplina calculo = new Disciplina("Calculo", 6, Arrays.asList());
		Disciplina biologia = new Disciplina("Biologia", 4, Arrays.asList());
		Disciplina algebra = new Disciplina("Algebra", 4, Arrays.asList());
		fisica.setHorariosFixos(Arrays.asList("SEG17", "SEG19"));
		Professor leonardo = new Professor("Leonardo");
		Professor ricardo = new Professor("Ricardo");
		Professor vanessa = new Professor("Vanessa");
		ricardo.addPreferencia(calculo);
		disciplinas.add(fisica);
		disciplinas.add(calculo);
		disciplinas.add(biologia);
		disciplinas.add(algebra);
		professores.add(leonardo);
		professores.add(ricardo);
		professores.add(vanessa);
		String algorit = "MinConflictsSolver"; 
		restricoesList = new ArrayList<>(
				Arrays.asList("HorarioDiferente", "ProfessorDiferente", "PreferenciaDisciplina", "HorarioFixo"));

		//Execucao principal ==============================
			CspListener.StepCounter<Variable, List<String>> stepCounter = new CspListener.StepCounter<>();
			AlgoritmoCtrl algoritmoCtrl = new AlgoritmoCtrl(disciplinas, professores, restricoesList);
			
			System.out.println("Alocar Professores ("+algorit+")");
			Timer timer = new Timer();
			Set<Optional<Assignment<Variable, List<String>>>> solucoesList =
					algoritmoCtrl.usarAlgoritmo(algorit, stepCounter);
			String tempo = timer.toString();
			
			long numResultados = solucoesList.size();
			System.out.println("Tempo decorrido = "+ tempo);
			System.out.println("Solucoes obtidas = "+ numResultados);
			System.out.println(stepCounter.getResults() + "\n");
		//Fim da Execucao principal =========================
		
		/*
		 * Transformar a lista de resultados em uma lista de objetos do tipo "Horario"
		 * contendo os blocos de aula com disciplinas e professores.
		 */
		GerenciadorDeResultados gerenciador = new GerenciadorDeResultados(disciplinas, professores, solucoesList);
		List<Horario> horarios = gerenciador.gerarHorarios();
		
		//Imprimir horarios
		int cont;
		for(Horario horario : horarios) {
			cont = 0;
			System.out.println("\t\tSEG \t\t  TER \t\t    QUA \t\tQUI \t\t  SEX");
			for(BlocoAula bloco : horario.getBlocosOrdenados()) {
		    	cont++;
		    	if(cont == 1) {
		    		System.out.print("17h-19h | ");
		    	}else if(cont == 6) {
		    		System.out.println();
		    		System.out.print("19h-21h | ");
		    	}else if(cont == 11) {
		    		System.out.println();
		    		System.out.print("21h-23h | ");
		    	}
		    	if(bloco.toString().equals("-")) {
		    		System.out.print("       --       | ");
		    	}else {
		    		if(bloco.getDisciplina().getProfessor() != null) {
		    			System.out.print(bloco + " | ");
		    		}else {
		    			System.out.print(bloco.getDisciplina()+"- NaoDef | ");
		    		}
		    	}
		    	if(cont == 15) {
		    		System.out.println();
		    	}
			}
			System.out.println("\n-------------------//-------------------");
		}
		System.out.println("Tempo decorrido = "+ tempo);
		System.out.println("Solucoes obtidas = "+ numResultados);
		System.out.println(stepCounter.getResults() + "\n");
	}
}