package alanfx.ProjetoCSP.entidades;

public class BlocoAula implements Cloneable {

	private String nome;
	private Disciplina disciplina;
	
	public BlocoAula(String nome) {
		this.nome = nome;
		this.disciplina = null;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@Override
	public String toString() {
		if(disciplina != null) {
			return disciplina + "-" + (disciplina.getProfessor() != null ? disciplina.getProfessor() : "semProf");
		}else {
			return "-";
		}
	}
	
	@Override
	public BlocoAula clone() {
		BlocoAula result;
		try {
			result = (BlocoAula) super.clone();
			result.nome = nome;
			result.disciplina = null;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException("BlocoAula nao pode ser clonado");
		}
		return result;
	}
}
