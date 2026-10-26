package org.example.modello;

public class Monopattino extends Veicolo {

    Double velocitaMassima;

    public Monopattino(String codice, Integer livelloBatteria, Stato stato, Double consumo, Double velocitaMassima) {
        super(codice, livelloBatteria, stato, consumo, 0.25);
        this.velocitaMassima = velocitaMassima;
    }

    @Override
    double calcolaCosto(int minuti) {
        return tariffa * minuti;
    }

    @Override
    double consumoBatteriaPerMinuto() {
        return consumo;
    }
}
