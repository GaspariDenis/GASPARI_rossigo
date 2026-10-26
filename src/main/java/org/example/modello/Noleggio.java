package org.example.modello;

import org.example.eccezioni.DataNonValidaException;

import java.util.Date;

//Noleggio (utente, veicolo, orario di inizio, orario di fine)
public class Noleggio {
    Utente utente;
    Veicolo veicolo;
    Date inizio;
    Date fine;

    public Noleggio(Utente utente, Veicolo veicolo, Date inizio, Date fine) throws DataNonValidaException {
        this.utente = utente;
        this.veicolo = veicolo;
        if(inizio.after(fine)){
            throw new DataNonValidaException("La data d'inizio è dopo quella di fine.", inizio, fine);
        }
        this.inizio = inizio;
        this.fine = fine;
    }
}
