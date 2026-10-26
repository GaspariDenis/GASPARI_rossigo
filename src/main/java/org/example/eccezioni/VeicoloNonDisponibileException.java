package org.example.eccezioni;

import org.example.modello.Stato;

public class VeicoloNonDisponibileException extends RossiGoException{
    Stato stato;

    public VeicoloNonDisponibileException(String msg, Stato stato) {
        super(msg);
        this.stato = stato;
    }
}
