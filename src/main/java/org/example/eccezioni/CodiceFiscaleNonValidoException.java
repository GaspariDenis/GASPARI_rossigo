package org.example.eccezioni;

public class CodiceFiscaleNonValidoException extends RossiGoException{
    String codiceFiscale;

    public CodiceFiscaleNonValidoException(String msg, String codiceFiscale) {
        super(msg);
        this.codiceFiscale = codiceFiscale;
    }
}
