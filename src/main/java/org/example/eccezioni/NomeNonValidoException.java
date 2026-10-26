package org.example.eccezioni;

public class NomeNonValidoException extends RossiGoException{
    String nome;

    public NomeNonValidoException(String msg, String nome) {
        super(msg);
        this.nome = nome;
    }
}
