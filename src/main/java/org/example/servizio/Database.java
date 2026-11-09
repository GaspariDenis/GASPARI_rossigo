package org.example.servizio;

import org.example.eccezioni.ErroreDatabaseException;
import org.example.modello.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private String url = "jdbc:sqlite:src/main/resources/database.db";

    private static Database instance;
    private Connection connection;

    private Database() throws ErroreDatabaseException {
        try {
            connection = DriverManager.getConnection(url);
        }catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public static Database getInstance() throws ErroreDatabaseException {
        if(instance == null)
            instance = new Database();
        return instance;
    }

    public void addVeicolo(Veicolo v) throws ErroreDatabaseException {
        try{
            if(connection == null || !connection.isValid(5)){
                throw new ErroreDatabaseException("Il database non è raggiungibile");
            }
        } catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }

        try{
            String query;
            if(v instanceof Bici){
                query = "insert into veicoli(codice, livelloBatteria, stato, consumo, tariffa, marce, tipo) values ( ?, ?, ?, ?, ?, ?, ?)";
            }else{
                query = "insert into veicoli(codice, livelloBatteria, stato, consumo, tariffa, velocitaMassima, tipo) values ( ?, ?, ?, ?, ?, ?, ?)";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, v.getCodice());
            statement.setInt(2, v.getLivelloBatteria());
            statement.setInt(3, v.getStato().ordinal());
            statement.setDouble(4, v.getConsumo());
            statement.setDouble(5, v.getTariffa());
            if(v instanceof Bici){
                statement.setInt(6, ((Bici) v).getMarce());
                statement.setString(7, "bici");
            }else if(v instanceof Monopattino){
                statement.setDouble(6, ((Monopattino) v).getVelocitaMassima());
                statement.setString(7, "monopattino");
            }
            statement.executeUpdate();
        }catch (Exception e){
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public ArrayList<Veicolo> getAllVeicoli() throws ErroreDatabaseException {
        try{
            if(connection == null || !connection.isValid(5)){
                throw new ErroreDatabaseException("Il database non è raggiungibile");
            }
        } catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }

        String query = "select * from veicoli";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            ArrayList<Veicolo> out = new ArrayList<>();
            while(set.next()){
                if(Objects.equals(set.getString("tipo"), "bici")){
                    int stat = set.getInt("stato");
                    out.add(new Bici(
                            set.getString("codice"),
                            set.getInt("livelloBatteria"),
                            (stat == 0) ? Stato.LIBERO : (stat == 2) ? Stato.IN_CARICA : Stato.IN_USO,
                            set.getDouble("consumo"),
                            set.getInt("marce"),
                            set.getDouble("tariffa")
                    ));
                }else{
                    int stat = set.getInt("stato");
                    out.add(new Monopattino(
                            set.getString("codice"),
                            set.getInt("livelloBatteria"),
                            (stat == 0) ? Stato.LIBERO : (stat == 2) ? Stato.IN_CARICA : Stato.IN_USO,
                            set.getDouble("consumo"),
                            set.getDouble("velocitaMassima")
                    ));
                }
            }
            return out;
        }catch (Exception e){
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public void updateVeicolo(Veicolo v) throws ErroreDatabaseException {
        try{
            if(connection == null || !connection.isValid(5)){
                throw new ErroreDatabaseException("Il database non è raggiungibile");
            }
        } catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }

        String query = "update veicoli set livelloBatteria = ?, stato = ? where codice = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, v.getLivelloBatteria());
            statement.setInt(2, Integer.parseInt(v.getStato().toString()));
            statement.setString(3, v.getCodice());
            statement.executeUpdate();
        }catch (Exception e){
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public void addUtente(Utente u) throws ErroreDatabaseException {
        try{
            if(connection == null || !connection.isValid(5)){
                throw new ErroreDatabaseException("Il database non è raggiungibile");
            }

            String query = "insert into utenti(id, nome, email, codiceFiscale, credito) values(?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, u.getId());
            statement.setString(2, u.getNome());
            statement.setString(3, u.getEmail());
            statement.setString(4, u.getCodiceFiscale());
            statement.setDouble(5, u.getCredito());
        } catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public ArrayList<Utente> getAllUtenti() throws ErroreDatabaseException {
        try{
            if(connection == null || !connection.isValid(5)){
                throw new ErroreDatabaseException("Il database non è raggiungibile");
            }

            String query = "select * from utenti";
            ArrayList<Utente> out = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(query);


            return out;
        } catch (Exception e) {
            throw new ErroreDatabaseException(e.getMessage());
        }
    }

    public void updateUtente(Utente u) throws ErroreDatabaseException {

    }
}
