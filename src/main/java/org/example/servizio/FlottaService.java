package org.example.servizio;

import org.example.eccezioni.*;
import org.example.modello.Stato;
import org.example.modello.Utente;
import org.example.modello.Veicolo;

import java.util.ArrayList;
import java.util.Objects;

public class FlottaService {
    ArrayList<Veicolo> listaVeicoli;
    ArrayList<Utente> listaUtenti;
    private Database db;

    private static FlottaService instance;

    private FlottaService() {

        try{
            db = Database.getInstance();
            listaVeicoli = db.getAllVeicoli();
            listaUtenti = db.getAllUtenti();
        }catch (Exception e){
            listaVeicoli = new ArrayList<>();
            listaUtenti = new ArrayList<>();
        }
    }

    public static FlottaService getInstance(){
        if(instance == null)
            instance = new FlottaService();
        return instance;
    }

    public void registraVeicolo(Veicolo veicolo) {
        try {
            db.addVeicolo(veicolo);
        }catch (Exception _){
        }
        listaVeicoli.add(veicolo);
    }

    public void registraUtente(Utente u){
        try {
            db.addUtente(u);
        }catch (Exception _){}
        listaUtenti.add(u);
    }

    public void sbloccaVeicolo(String codice, String idUtente) throws RossiGoException {
        Veicolo v = veicoloExist(codice);
        if(v == null){
            throw new VeicoloNonTrovatoException("Veicolo: " + codice +" non trovato.", codice);
        }
        Stato s = v.getStato();

        if(v.getLivelloBatteria() <= 15){
            mettiInRicarica(codice);
        }

        if(s == Stato.IN_CARICA){
            throw new BatteriaScaricaException("Batteria in ricarica", v.getLivelloBatteria());
        }

        if(s == Stato.IN_USO) {
            throw new VeicoloNonDisponibileException("Veicolo già utilizzato da un utente.", Stato.IN_USO);
        }


        Utente u = userExist(idUtente);
        if(u == null){
            throw new UtenteNonTrovatoException("Utente: " + idUtente + " non esiste.", idUtente);
        }

        if(u.getCredito() < 1) {
            throw new CreditoInsufficienteException("Il credito dell'utente :" + idUtente + " è insufficente.", u.getCredito());
        }
        v.setStato(Stato.IN_USO);

        updateVeicolo(v);
    }

    public void terminaNoleggio(String codice, String idUtente, int minutiUsati) throws RossiGoException {
        Veicolo v = veicoloExist(codice);
        if(v == null){
            throw new VeicoloNonTrovatoException("Veicolo: " + codice +" non trovato.", codice);
        }

        Utente u = userExist(idUtente);
        if(u == null){
            throw new UtenteNonTrovatoException("Utente: " + idUtente + " non esiste.", idUtente);
        }

        double costo = v.calcolaCosto(minutiUsati) + 1;
        if(costo > u.getCredito()){
            throw new CreditoInsufficienteException("Il credito dell utente: "+ idUtente +" è troppo basso", u.getCredito());
        }
        u.setCredito(u.getCredito() - costo);

        double consumo = v.consumoBatteriaPerMinuto() * minutiUsati;

        v.setLivelloBatteria((v.getLivelloBatteria() - consumo < 0)? 0: (int) (v.getLivelloBatteria() - consumo));

        if(v.getLivelloBatteria() <= 15){
            mettiInRicarica(codice);
        }else{
            v.setStato(Stato.LIBERO);
        }
        updateUtente(u);
        updateVeicolo(v);
    }

    public void mettiInRicarica(String codice) throws VeicoloNonTrovatoException {
        Veicolo v = veicoloExist(codice);
        if(v == null){
            throw new VeicoloNonTrovatoException("Veicolo: " + codice +" non trovato.", codice);
        }

        v.setStato(Stato.IN_CARICA);
        Thread.startVirtualThread(()-> {
            try{
                Thread.sleep(10000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            v.setLivelloBatteria(100);
            v.setStato(Stato.LIBERO);
        });
        updateVeicolo(v);
    }

    private Utente userExist(String id) {
        for(Utente u : listaUtenti){
            if(id.equals(u.getId())){
                return u;
            }
        }
        return null;
    }

    private Veicolo veicoloExist(String id) {
        for(Veicolo u : listaVeicoli){
            if(id.equals(u.getCodice())){
                return u;
            }
        }
        return null;
    }

    private void updateVeicolo(Veicolo v) {
        try{
            db.updateVeicolo(v);
        }catch (Exception _){
        }
    }
    private void updateUtente(Utente u) {
        try {
            db.updateUtente(u);
        }catch (Exception _){}
    }
}
