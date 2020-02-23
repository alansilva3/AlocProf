package alanfx.ProjetoCSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CspListener;
import aima.core.search.csp.Variable;
import alanfx.ProjetoCSP.csp.AlgoritmoCtrl;
import alanfx.ProjetoCSP.csp.AlocCSP;
import alanfx.ProjetoCSP.entidades.BlocoAula;
import alanfx.ProjetoCSP.entidades.Disciplina;
import alanfx.ProjetoCSP.entidades.Horario;
import alanfx.ProjetoCSP.entidades.Professor;
import alanfx.ProjetoCSP.persistencia.Persistencia;
import alanfx.ProjetoCSP.utils.GerenciadorDeResultados;
import alanfx.ProjetoCSP.utils.Timer;

public class Main {
	
	private static List<Disciplina> disciplinas = new ArrayList<>();
	private static List<Professor> professores = new ArrayList<>();
	private static List<String> restricoesList;
	private static List<Variable> variaveis;
	private static List<List<String>> valores;
	
	public static void main(String[] args) {
		
		//Adicionando dados para teste ===================================
			Disciplina fisica = new Disciplina("Fisica", 4);
			Disciplina calculo = new Disciplina("Calculo", 6);
			Disciplina biologia = new Disciplina("Biologia", 4);
			Disciplina algebra = new Disciplina("Algebra", 4);
//			fisica.setHorariosFixos(Arrays.asList("SEG17", "SEG19"));
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
			//Exemplo algoritmo selecionado
			String algorit = "MinConflictsSolver"; 
			//Exemplo de instanciacao da lista de restricoes
			restricoesList = new ArrayList<>(
					Arrays.asList());
			variaveis = AlocCSP.criarVariaveis(disciplinas);
			valores = AlocCSP.createValues(AlocCSP.criarProfessores(professores), AlocCSP.aulas);
		// Fim - Dados adicionados para teste ===================================
		
		
//		List<Disciplina> list = new Persistencia().getDisciplinasFromJson();
//		if (!list.isEmpty())
//		for (Disciplina dis : list){
//			System.out.println(dis.getNome());
//		}

		//Execucao principal ==============================
			CspListener.StepCounter<Variable, List<String>> stepCounter = new CspListener.StepCounter<>();
			AlgoritmoCtrl algoritmoCtrl = new AlgoritmoCtrl(disciplinas, professores, restricoesList, variaveis, valores);
			
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
		 * Essa lista de horarios sera usada para gerar os resultados na interface.
		 */
		GerenciadorDeResultados gerenciador = new GerenciadorDeResultados(disciplinas, professores, solucoesList);
		List<Horario> horarios = gerenciador.gerarHorarios();
		
		
		//ESSA PARTE SERA DESCARTADA DEPOIS DE CRIAR A INTERFACE GRAFICA
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