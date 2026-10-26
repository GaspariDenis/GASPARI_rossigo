package org.example.eccezioni;

public class UtenteNonTrovatoException extends RossiGoException{
    String id;

    public UtenteNonTrovatoException(String msg, String id) {
        super(msg);
        this.id = id;
    }
}
