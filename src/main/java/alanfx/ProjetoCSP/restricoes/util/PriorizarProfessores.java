package alanfx.projetocsp.restricoes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

/**
 * Representa uma restrição unária que proibe uma disciplina de atribuir "sem professor" 
 * quando ainda houver professor disponível para ser atribuido.
 * 
 */
public class PriorizarProfessores<VAR extends Variable, VAL> implements Constraint<VAR, List<String>> {

	private VAR var;
	private List<String> profs;
	private Map<String, List<VAR>> preferencias;
	private List<VAR> scope;

	public PriorizarProfessores(VAR var, List<String> profs, Map<String, List<VAR>> preferencias) {
		this.var = var;
		this.profs = profs;
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
		if(!value.get(1).equals("semProf")){
			return true;
		}else {//conferir se algum professor da lista, não foi atribuido
			//antes, preciso retirar da lista atual de professores aqueles que nao tiver a disciplina atual como preferida
			List<String> profs2 = new ArrayList<>();
			for(String prof : profs) {
				if(!preferencias.containsKey(prof)) continue;
				if(preferencias.get(prof).contains(var)) {
					profs2.add(prof);//adiciona o prof q tem a disciplina atual em sua lista de preferencias
				}
			}
			boolean contem = false;
    		for(String prof : profs2) {
    			for (Map.Entry<VAR, List<String>> entry : assignment.getVariableToValueMap().entrySet()) {
    				if(prof.equals(entry.getValue().get(1))) {
    					contem=true;
    				}
    			}
    			if(!contem) {//algum professor nao foi atribuido ainda
    				return false;	
    			}
    			contem = false;
    		}
    		return true;//todos os professores foram atribuidos, então usar "sem professor" para as demais atribuições
		}
	}
}