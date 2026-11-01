package org.example.eccezioni;

public class VeicoloNonTrovatoException extends RossiGoException {
    public String codice;

    public VeicoloNonTrovatoException(String message, String codice) {
        super(message);
        this.codice = codice;
    }
}
