package alanfx.projetocsp.restricoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

/**
 * Representa uma restrição unária que associa um professor a um conjunto de disciplinas preferidas.
 * 
 */
public class PreferenciaDisciplina<VAR extends Variable, VAL> implements Constraint<VAR, List<String>> {

	private VAR var;
	private Map<String, List<VAR>> preferencias;
	private List<VAR> scope;

	public PreferenciaDisciplina(VAR var, Map<String, List<VAR>> preferencias) {
		this.var = var;
		this.preferencias = preferencias;
		scope = new ArrayList<>(1);
		scope.add(var);
	}

	@Override
	public List<VAR> getScope() {
		return scope;
	}

	@Override
	public boolean isSatisfiedWith(Assignment<VAR, List<String>> assignment) {
		List<String> value = assignment.getValue(var);
		if(preferencias.containsKey(value.get(1))) {//Verifica se o professor tem preferencias
			return preferencias.get(value.get(1)).contains(var);//verifica se a disciplina esta na lista de preferencias
		}else {
			return true;
		}
	}
}