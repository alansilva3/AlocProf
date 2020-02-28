package alanfx.projetocsp.restricoes;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

/**
 * Representa uma restrição binária que proíbe duas disciplinas de ter o mesmo horário.
 * 
 */
public class HorarioDiferente<VAR extends Variable, VAL> implements Constraint<VAR, List<String>> {

	private VAR var1;
	private VAR var2;
	private List<VAR> scope;

	public HorarioDiferente(VAR var1, VAR var2) {
		this.var1 = var1;
		this.var2 = var2;
		scope = new ArrayList<>(2);
		scope.add(var1);
		scope.add(var2);
	}

	@Override
	public List<VAR> getScope() {
		return scope;
	}

	@Override
	public boolean isSatisfiedWith(Assignment<VAR, List<String>> assignment) {
		List<String> value1 = assignment.getValue(var1);
		List<String> value2 = assignment.getValue(var2);
		if(value1 == null || value2 == null) return true;
		return !value1.get(0).equals(value2.get(0));
	}
}