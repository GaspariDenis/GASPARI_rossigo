package org.example.modello;

public abstract class Veicolo {
    public String getCodice() {
        return codice;
    }

    String codice;

    public Integer getLivelloBatteria() {
        return livelloBatteria;
    }

    public void setLivelloBatteria(Integer livelloBatteria) {
        this.livelloBatteria = livelloBatteria;
    }

    Integer livelloBatteria;

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

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

    public abstract double calcolaCosto(int minuti);

    public abstract double consumoBatteriaPerMinuto();
}

