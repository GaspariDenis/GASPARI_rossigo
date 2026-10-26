package org.example.eccezioni;

public class EmailNonValidaException extends RossiGoException{
    String email;

    public EmailNonValidaException(String msg, String email) {
        super(msg);
        this.email = email;
    }
}
