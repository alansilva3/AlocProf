package alanfx.projetocsp.persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import alanfx.projetocsp.entidades.Disciplina;
import alanfx.projetocsp.entidades.Horario;
import alanfx.projetocsp.entidades.Professor;

public class Persistencia {
    private Gson gson = new Gson();

    private void salvar(String name, String json) {
        try{
            FileWriter writeFile = new FileWriter(name+".json");
            //Escreve no arquivo conteudo do Objeto JSON
            writeFile.write(json);
            writeFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<Horario> getHorariosFromJson() {
        List<Horario> list = new ArrayList<>();
        try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
            Type horariosListType = new TypeToken<ArrayList<Horario>>(){}.getType();
            list = gson.fromJson(new FileReader(
                    "horarios.json"), horariosListType);
        }
        //Trata as exceptions que podem ser lançadas no decorrer do processo
        catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    public List<Disciplina> getDisciplinasFromJson() {
        List<Disciplina> list = new ArrayList<>();
        try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
            Type disciplinaListType = new TypeToken<ArrayList<Disciplina>>(){}.getType();
            list = gson.fromJson(new FileReader(
                    "disciplinas.json"), disciplinaListType);
        }
        //Trata as exceptions que podem ser lançadas no decorrer do processo
        catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    public List<Professor> getProfessoresFromJson() {
        List<Professor> list = new ArrayList<>();
        try {
            //Salva no objeto JSONObject o que o parse tratou do arquivo
            Type professorListType = new TypeToken<ArrayList<Professor>>(){}.getType();
            list = gson.fromJson(new FileReader(
                    "professores.json"), professorListType);
        }
        //Trata as exceptions que podem ser lançadas no decorrer do processo
        catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    public void salvarDisciplinas(List<Disciplina> disciplinas) {
        String json = gson.toJson(disciplinas);
        salvar("disciplinas", json);
    }

    public void salvarProfessores(List<Professor> professores) {
        String json = gson.toJson(professores);
        salvar("professores", json);
    }

    public void salvarHorarios(List<Horario> horarios) {
        String json = gson.toJson(horarios);
        salvar("horarios", json);
    }

}
