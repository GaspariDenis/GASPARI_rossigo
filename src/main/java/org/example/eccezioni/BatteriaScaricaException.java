package org.example.eccezioni;

public class BatteriaScaricaException extends RossiGoException{

    Integer batteria;

    public BatteriaScaricaException(String msg, Integer batteria) {
        super(msg);
        this.batteria = batteria;
    }
}
