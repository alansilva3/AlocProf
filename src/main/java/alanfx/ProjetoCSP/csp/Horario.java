package alanfx.ProjetoCSP.csp;

import java.util.ArrayList;
import java.util.List;

public class Horario {

	private List<BlocoAula> blocos;
	

	public Horario(List<BlocoAula> blocos) {
		this.blocos = blocos;
	}

	public List<BlocoAula> getBlocos() {
		return blocos;
	}
	
	public List<BlocoAula> getBlocosOrdenados() {
		/*
		 * Os blocos de horario que não tiver com disciplina alocada 
		 * estara como "null" no campo "disciplina"
		 */
		return ordenar(blocos); 
	}

	private List<BlocoAula> ordenar(List<BlocoAula> blocos2) {
		List<BlocoAula> blocosOrdenados = new ArrayList<>();
		for(String aula : AlocCSP.aulas) {
			for(BlocoAula bloco : blocos) {
				if(bloco.getNome().equals(aula)) {
					blocosOrdenados.add(bloco);
					break;
				}
			}
		}
		return blocosOrdenados;
	}
}
