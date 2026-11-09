package org.example.modello;

public class Monopattino extends Veicolo {

    public Double getVelocitaMassima() {
        return velocitaMassima;
    }

    Double velocitaMassima;

    public Monopattino(String codice, Integer livelloBatteria, Stato stato, Double consumo, Double velocitaMassima) {
        super(codice, livelloBatteria, stato, consumo, 0.25);
        this.velocitaMassima = velocitaMassima;
    }

    @Override
    public double calcolaCosto(int minuti) {
        return tariffa * minuti;
    }

    @Override
    public double consumoBatteriaPerMinuto() {
        return consumo;
    }
}
