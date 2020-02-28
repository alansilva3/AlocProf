package alanfx.projetocsp.csp;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CspHeuristics;
import aima.core.search.csp.CspListener.StepCounter;
import aima.core.search.csp.CspSolver;
import aima.core.search.csp.FlexibleBacktrackingSolver;
import aima.core.search.csp.MinConflictsSolver;
import aima.core.search.csp.Variable;
import alanfx.projetocsp.entidades.Disciplina;
import alanfx.projetocsp.entidades.Professor;
import alanfx.projetocsp.restricoes.util.ValorAtribuido;

public class AlgoritmoCtrl {
	
	private List<Disciplina> disciplinas;
	private List<Professor> professores;
	private List<String> restricoesList;
	private List<Variable> variaveis;
	private List<List<String>> valores;
	
	public AlgoritmoCtrl(List<Disciplina> disciplinas, List<Professor> professores, List<String> restricoesList) {
		this.disciplinas = disciplinas;
		this.professores = professores;
		this.restricoesList = restricoesList;
		this.variaveis = AlocCSP.criarVariaveis(disciplinas);
        this.valores = AlocCSP.createValues(AlocCSP.criarProfessores(professores), AlocCSP.aulas);
	}

	public Set<Optional<Assignment<Variable, List<String>>>> usarAlgoritmo(String algorit,
			StepCounter<Variable, List<String>> stepCounter) {
		CspSolver<Variable, List<String>> solver;
		switch(algorit) {
			case "MinConflictsSolver":
				solver = new MinConflictsSolver<>(1000);
				solver.addCspListener(stepCounter);
				stepCounter.reset();
				return getSolucoes(solver);
			case "Backtracking + MRV & DEG + LCV + AC3":
				solver = new FlexibleBacktrackingSolver<Variable, List<String>>().setAll();
				solver.addCspListener(stepCounter);
				stepCounter.reset();
				return getSolucoes(solver);
			case "Backtracking + MRV & DEG":
				solver = new FlexibleBacktrackingSolver<Variable, List<String>>().set(CspHeuristics.mrvDeg());
				solver.addCspListener(stepCounter);
				stepCounter.reset();
				return getSolucoes(solver);
			case "Backtracking":
				solver = new FlexibleBacktrackingSolver<>();
				solver.addCspListener(stepCounter);
				stepCounter.reset();
				return getSolucoes(solver);
			default:
				return new HashSet<>();
		}
	}

	private Set<Optional<Assignment<Variable, List<String>>>> getSolucoes(CspSolver<Variable, List<String>> solver) {
		Optional<Assignment<Variable, List<String>>> solution;
		Set<Optional<Assignment<Variable, List<String>>>> set = new HashSet<>();
		// long cont = 0;
		for (Variable var : variaveis) {
			for (List<String> val : valores) {
				// cont++;
				// System.out.println(cont);
				CSP<Variable, List<String>> csp = new AlocCSP(disciplinas, professores, restricoesList, new ValorAtribuido<>(var, val));
				solution = solver.solve(csp);
				if(!solution.isEmpty())
					set.add(solution);
			}
		}
		return set;
	}
}
