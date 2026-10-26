package org.example.servizio;

import org.example.eccezioni.UtenteNonTrovatoException;
import org.example.modello.Utente;
import org.example.modello.Veicolo;

import java.util.ArrayList;
import java.util.Objects;

public class FlottaService {
    ArrayList<Veicolo> listaVeicoli;
    ArrayList<Utente> listaUtenti;

    private static FlottaService instance;

    private FlottaService() {
        listaVeicoli = new ArrayList<>();
        listaUtenti = new ArrayList<>();
    }

    static FlottaService getInstance(){
        if(instance == null)
            instance = new FlottaService();
        return instance;
    }

    public void registraVeicolo(Veicolo veicolo){
        listaVeicoli.add(veicolo);
    }

    public void registraUtente(Utente u){
        listaUtenti.add(u);
    }

    public void sbloccaVeicolo(String codice, String idUtente){

    }

    private boolean userExist(String id) {
        for(Utente u : listaUtenti){
            if(id.equals(u.getId())){
                return true;
            }
        }
        return false;
    }
}
