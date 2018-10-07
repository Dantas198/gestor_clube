package model;

import data.FacadeData;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class Clube implements Serializable {

    private FacadeData data;
    private Map<Integer,Aluno> alunos;

    // Construtor
    public Clube() {

        Clube clube;
        data = FacadeData.getInstance();

        try {
            clube = (Clube) data.accessState();
            this.alunos = clube.getAlunos();
        } catch (Exception e) {
            this.alunos = new LinkedHashMap<>();
        }

        System.out.println(this.alunos);
    }

    /**
     * Adicionar um membro.
     * Se o membro já existe, não faz nada
     */
    public Map<Integer, Aluno> getAlunos(){
        Map<Integer,Aluno> res = new LinkedHashMap<>();
        for(Aluno a : this.alunos.values()) {
            res.put(a.getNumero(), a.clone());
        }
        return res;
    }

    /**
     * Adicionar um membro.
     * Se o membro já existe, não faz nada
     */
    public void pagarQuota(Integer numero, Double valor) throws IOException{
        Aluno a = alunos.get(numero);
        a.addCota(valor);
        alunos.put(numero, a);
        data.saveState(this);
        System.out.println("Guardado");
    }


    public Aluno getAluno(int num) throws AlunoNaoExisteException {

        Aluno a = this.alunos.get(num);
        if(a == null) {
            throw new AlunoNaoExisteException(num);
        } else {
            return a.clone();
        }
    }

    /**
     * Adicionar um membro.
     * Se o membro já existe, não faz nada
     */
    public void addAluno(Aluno a) throws AlunoJaExisteException, IOException {

        Aluno copia = a.clone();
        int num = a.getNumero();

        Aluno aluno = alunos.putIfAbsent(num, copia);
        if(aluno != null) { //Aluno já existe
            throw new AlunoJaExisteException(num);
        }

        data.saveState(this); //Guarda o estado atual do clube
    }

    public void editAluno(Aluno a) throws AlunoNaoExisteException, IOException {
        int num = a.getNumero();

        Aluno aluno = this.getAluno(num);
        aluno.setNome(a.getNome());
        aluno.setCurso(a.getCurso());
        aluno.setMorada(a.getMorada());

        alunos.put(num, aluno);
        data.saveState(this);
    }


    public void delAluno(int num) throws AlunoNaoExisteException, IOException {

        Aluno aluno = alunos.remove(num);

        if (aluno == null) { //Aluno não existe
            throw new AlunoNaoExisteException(num);
        }

        data.saveState(this); //Guarda o estado atual do clube
    }

    public static void main(String args[]){
        Aluno j = new Aluno("marco", 8, "mie", LocalDate.now(), "");
        Aluno w = new Aluno("maro", 9, "mie", LocalDate.now(), "");
        Aluno i = new Aluno("marc", 10, "mii", LocalDate.now(), "");
        Clube c = new Clube();

        try{c.addAluno(j); c.addAluno(i); c.addAluno((w)); c.pagarQuota(8,5.0); c.pagarQuota(9,10.0);
        c.pagarQuota(10, 20.0);}
        catch(AlunoJaExisteException e){}
        catch (IOException e){}

        Map<Integer, Aluno> a = c.getAlunos();
        for(Aluno al : a.values()) {
            System.out.println(al.getNome());
            for(Cota cot : al.getCotas()){
                System.out.println(cot.getValor());
            }
        }
    }
}
