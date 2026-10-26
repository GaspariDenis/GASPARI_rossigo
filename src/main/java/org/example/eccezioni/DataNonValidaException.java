package org.example.eccezioni;

import java.util.Date;

public class DataNonValidaException extends RossiGoException{
    Date inizio;
    Date fine;

    public DataNonValidaException(String msg, Date inizio, Date fine) {
        super(msg);
        this.inizio = inizio;
        this.fine = fine;
    }
}
