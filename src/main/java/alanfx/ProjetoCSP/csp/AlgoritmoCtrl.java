package alanfx.ProjetoCSP.csp;

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
import alanfx.ProjetoCSP.entidades.Disciplina;
import alanfx.ProjetoCSP.entidades.Professor;
import alanfx.ProjetoCSP.restricoes.util.ValorAtribuido;

public class AlgoritmoCtrl {
	
	private static List<Disciplina> disciplinas;
	private static List<Professor> professores;
	private static List<String> restricoesList;
	private static List<Variable> variaveis;
	private static List<List<String>> valores;
	
	public AlgoritmoCtrl(List<Disciplina> disciplinas, List<Professor> professores, List<String> restricoesList,
			List<Variable> variaveis, List<List<String>> valores) {
		super();
		AlgoritmoCtrl.disciplinas = disciplinas;
		AlgoritmoCtrl.professores = professores;
		AlgoritmoCtrl.restricoesList = restricoesList;
		AlgoritmoCtrl.variaveis = variaveis;
		AlgoritmoCtrl.valores = valores;
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
		for (Variable var : variaveis) {
			for (List<String> val : valores) {
				CSP<Variable, List<String>> csp = new AlocCSP(disciplinas, professores, restricoesList, new ValorAtribuido<>(var, val));
				solution = solver.solve(csp);
				if(!solution.isEmpty())
					set.add(solution);
			}
		}
		return set;
	}
}
