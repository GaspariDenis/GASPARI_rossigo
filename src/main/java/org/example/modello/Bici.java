package org.example.modello;

public class Bici  extends Veicolo{

    Integer marce;

    public Bici(String codice, Integer livelloBatteria, Stato stato, Double consumo, Integer marce, Double tariffa) {
        super(codice, livelloBatteria, stato, consumo, 0.15);
        this.marce = marce;
    }

    @Override
    double calcolaCosto(int minuti) {
        return tariffa * minuti;
    }

    @Override
    double consumoBatteriaPerMinuto() {
        return consumo;
    }

    Integer getNumeroMarce() {
        return marce;
    }
}
