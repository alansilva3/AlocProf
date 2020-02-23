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
	
//	//Lista de algoritmos que poderao ser selecionados pelo usuario (apenas um sera selecionado por vez)
//	private static final List<String> algoritmos = new ArrayList<>(
//		Arrays.asList("MinConflictsSolver",
//					  "Backtracking + MRV & DEG + LCV + AC3",
//					  "Backtracking + MRV & DEG",
//					  "Backtracking"));
//	
//	//Lista de restricoes que poderao ser selecionadas pelo usuario
//	private static final List<String> restricoesPossiveis = new ArrayList<>( 
//		Arrays.asList("HorarioDiferente", "ProfessorDiferente", "PreferenciaDisciplina", "HorarioFixo"));
	
	private static List<Disciplina> disciplinas = new ArrayList<>();
	private static List<Professor> professores = new ArrayList<>();
	private static List<String> restricoesList;
	private static List<Variable> variaveis;
	private static List<List<String>> valores;
	
	public static void main(String[] args) {
		
		Disciplina fisica = new Disciplina("Fisica", 4);
		Disciplina calculo = new Disciplina("Calculo", 6);
		Disciplina biologia = new Disciplina("Biologia", 4);
		
		fisica.setHorariosFixos(Arrays.asList("SEG17", "SEG19"));
		
		Professor leonardo = new Professor("Leonardo");
		Professor estombelo = new Professor("Estombelo");
		Professor maria = new Professor("Maria");
		Professor ana = new Professor("Ana");
		
		estombelo.addPreferencia(calculo);
		
		disciplinas.add(fisica);
		disciplinas.add(calculo);
		disciplinas.add(biologia);
		
		professores.add(leonardo);
		professores.add(estombelo);
		professores.add(maria);
		professores.add(ana);
		
		String algorit = "MinConflictsSolver"; //Exemplo algoritmo selecionado
		
		//Exemplo de instanciacao da lista de restricoes
		restricoesList = new ArrayList<>(
				Arrays.asList("HorarioDiferente", "ProfessorDiferente", "PreferenciaDisciplina", "HorarioFixo"));

		List<Disciplina> list = new Persistencia().getDisciplinasFromJson();
		if (!list.isEmpty())
		for (Disciplina dis : list){
			System.out.println(dis.getNome());
		}

		variaveis = AlocCSP.criarVariaveis(disciplinas);
		valores = AlocCSP.createValues(AlocCSP.criarProfessores(professores), AlocCSP.aulas);
		
		//Execucao principal ==============================
		CspListener.StepCounter<Variable, List<String>> stepCounter = new CspListener.StepCounter<>();
		AlgoritmoCtrl algoritmoCtrl = new AlgoritmoCtrl(disciplinas, professores, restricoesList, variaveis, valores);
		
		System.out.println("Alocar Professores ("+algorit+")");
		Timer timer = new Timer();
		Set<Optional<Assignment<Variable, List<String>>>> solucoesList =
				algoritmoCtrl.usarAlgoritmo(algorit, stepCounter);
		
		System.out.println("Tempo decorrido = "+ timer);
		long numResultados = solucoesList.size();
		System.out.println("Numero de resultados = "+ numResultados);
		System.out.println(stepCounter.getResults() + "\n");
		
		
		/*
		 * Transformar a lista de resultados em uma lista de objetos do tipo "Horario"
		 * contendo os blocos de aula com disciplinas e professores
		 */
		GerenciadorDeResultados gerenciador = new GerenciadorDeResultados(disciplinas, professores, solucoesList);
		
		//Usar essa lista pra gerar os resultados na interface
		List<Horario> horarios = gerenciador.gerarHorarios();
		
		
		//ESSA PARTE SERÁ DESCARTADA DEPOIS DE CRIAR A INTERFACE GRAFICA
		int cont = 0;
		for(Horario horario : horarios) {
			cont = 0;
			for(BlocoAula bloco : horario.getBlocosOrdenados()) {
		    	cont++;
		    	System.out.print(bloco + "  ");
		    	if(cont == 5 || cont == 10 || cont == 15) {
	    			System.out.println();
	    		}
			}
			System.out.println("-------------------//-------------------");
		}
	}
}