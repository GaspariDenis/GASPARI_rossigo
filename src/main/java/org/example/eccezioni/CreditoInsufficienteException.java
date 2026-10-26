package org.example.eccezioni;

public class CreditoInsufficienteException extends RossiGoException{
    Double credito;

    public CreditoInsufficienteException(String msg, Double credito) {
        super(msg);
        this.credito = credito;
    }
}
