package alanfx.ProjetoCSP.csp.entidades;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Variable;

public class Professor implements Cloneable {
	private String nome;
	private List<Variable> preferencias;
	
	//Carga horaria do professor para o resultado atual
	private Integer cargaHoraria;
	private List<String> horariosAlocados;
	
	public Professor(String nome) {
		this.nome = nome;
		this.preferencias = new ArrayList<>();
		this.cargaHoraria = 0;
		this.horariosAlocados = new ArrayList<>();
	}
	
	public List<String> getHorariosAlocados() {
		return horariosAlocados;
	}

	public void addHorarioAlocado(String horario) {
		incrementCargaHoraria(30); //Valor referente a cada duas aulas semanais (2cr)
		this.horariosAlocados.add(horario);
	}

	public String getNome() {
		return nome;
	}

	public List<Variable> getPreferencias() {
		return preferencias;
	}
	
	public void addPreferencia(Disciplina disc) {
		for(Variable var : disc.getVars()) {
			this.preferencias.add(var);
		}
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public void incrementCargaHoraria(Integer valor) {
		this.cargaHoraria += valor;
	}
	
	@Override
	public String toString() {
		return nome;
	}

	@Override
	public Professor clone() {
		Professor result;
		try {
			result = (Professor) super.clone();
			result.nome = nome;
			result.preferencias = new ArrayList<>();
			result.cargaHoraria = 0;
			result.horariosAlocados = new ArrayList<>();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException("Professor nao pode ser clonado");
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Professor other = (Professor) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
