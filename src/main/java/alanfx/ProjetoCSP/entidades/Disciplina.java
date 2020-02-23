package alanfx.ProjetoCSP.entidades;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Variable;

public class Disciplina implements Cloneable {
	private String nome;
	private Integer cred;
	private List<Variable> vars;
	private List<String> horariosFixos;

	// Aqui sera armazenado a lista de horarios do resultado atual
	private List<String> horariosAlocados;

	private Professor professor;

	public Disciplina(String nome, Integer cred) {
		this.nome = nome;
		this.cred = cred;
		this.vars = criarVariaveis();
		this.horariosFixos = new ArrayList<>();
		this.horariosAlocados = new ArrayList<>();
		this.professor = null;
	}

	private List<Variable> criarVariaveis() {
		List<Variable> vars = new ArrayList<>();
		if (cred == 2) {
			vars.add(new Variable(nome + "01"));
		} else if (cred == 4) {
			vars.add(new Variable(nome + "01"));
			vars.add(new Variable(nome + "02"));
		} else {
			vars.add(new Variable(nome + "01"));
			vars.add(new Variable(nome + "02"));
			vars.add(new Variable(nome + "03"));
		}
		return vars;
	}

	public List<Variable> getVars() {
		return vars;
	}

	public List<String> getHorariosFixos() {
		return horariosFixos;
	}

	public void setHorariosFixos(List<String> horarios) {
		this.horariosFixos = horarios;
	}

	public String getNome() {
		return nome;
	}

	public Integer getCred() {
		return cred;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public List<String> getHorariosAlocados() {
		return horariosAlocados;
	}

	public void addHorarioAlocado(String horario) {
		this.horariosAlocados.add(horario);
	}

	public boolean contem(Variable var) {
		return vars.contains(var);
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public Disciplina clone() {
		Disciplina result;
		try {
			result = (Disciplina) super.clone();
			result.nome = nome;
			result.cred = cred;
			result.vars = new ArrayList<>(vars);
			result.horariosFixos = new ArrayList<>(horariosFixos);
			result.horariosAlocados = new ArrayList<>();
			result.professor = null;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException("Disciplina nao pode ser clonada");
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
		Disciplina other = (Disciplina) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
