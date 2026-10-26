package org.example.modello;

public abstract class Veicolo {
    String codice;
    Integer livelloBatteria;
    Stato stato;
    Double consumo;
    Double tariffa;

    public Veicolo(String codice, Integer livelloBatteria, Stato stato, Double consumo, Double tariffa) {
        this.codice = codice;
        this.livelloBatteria = livelloBatteria;
        this.stato = stato;
        this.consumo = consumo;
        this.tariffa = tariffa;
    }

    abstract double calcolaCosto(int minuti);

    abstract double consumoBatteriaPerMinuto();
}

