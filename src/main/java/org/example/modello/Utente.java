package org.example.modello;

import it.kamaladafrica.codicefiscale.CodiceFiscale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.eccezioni.*;

//id, nome, email, codice fiscale, credito in euro
public class Utente {
    String id;
    String nome;
    String email;
    String codiceFiscale;
    Double credito;

    public Utente(String id, String nome, String email, String codiceFiscale, Double credito) throws RossiGoException {
        this.id = id;
        if(StringUtils.isBlank(nome)){
            throw new NomeNonValidoException("Il nome non è valido.", nome);
        }
        this.nome = nome;

        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(email)){
            throw new CodiceFiscaleNonValidoException("Il codice fiscale: "+ codiceFiscale + " non è valido.", codiceFiscale);
        }
        this.email = email;
        if(!CodiceFiscale.isFormatValid(codiceFiscale)) {
            throw new EmailNonValidaException("La mail: " + email + " non è valida.", email);
        }
        this.codiceFiscale = codiceFiscale;
        if(credito < 1){
            throw new CreditoInsufficienteException("Il credito è insufficente.", credito);
        }
        this.credito = credito;
    }

    public String getId() {
        return id;
    }
}
