package org.example.app;

import com.github.lalyos.jfiglet.FigletFont;
import org.example.eccezioni.RossiGoException;
import org.example.modello.Bici;
import org.example.modello.Monopattino;
import org.example.modello.Stato;
import org.example.modello.Utente;
import org.example.servizio.FlottaService;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static java.lang.IO.println;

@CommandLine.Command(name = "rossigo",
        mixinStandardHelpOptions = true,
        version = "RossiGo 1.0",
        description = "CLI per la gestione della flotta di sharing RossiGo.",
        subcommands = {
                Main.SbloccaCommand.class,
                Main.TerminaCommand.class,
                Main.SimulaCommand.class
        })
public class Main implements Callable<Integer> {

    private static final FlottaService servizio = FlottaService.getInstance();

    static {
        // Setup dei dati iniziali
        try {
            servizio.registraVeicolo(new Bici("BK-0042", 80, Stato.LIBERO, 0.20, 4, 2d));
            servizio.registraVeicolo(new Monopattino("SC-0107", 90, Stato.LIBERO, 0.3, 65d));
            servizio.registraVeicolo(new Monopattino("SC-0222", 9, Stato.LIBERO, 0.3, 65d)); // Batteria scarica

            servizio.registraUtente(new Utente("U01", "Mario Rossi", "mario@email.com", "RSSMRA80A01L840J", 15.0));
            servizio.registraUtente(new Utente("U03", "Anna Verdi", "anna@email.com", "VRDMRA80A41L840J", 0.50)); // Credito insufficiente
        } catch (Exception ignored) {
        }
    }

    @Override
    public Integer call() {
        try {
            println(FigletFont.convertOneLine("RossiGo"));
        } catch (Exception ignored) {
        }

        CommandLine.usage(this, System.out);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    // sblocca
    @CommandLine.Command(name = "sblocca", description = "Sblocca un veicolo per iniziare un noleggio")
    static class SbloccaCommand implements Callable<Integer> {

        @CommandLine.Option(names = {"-u", "--utente"}, required = true, description = "ID dell'utente che richiede il noleggio")
        private String idUtente;

        @CommandLine.Parameters(index = "0", description = "Codice del veicolo da sbloccare (es. BK-0042)")
        private String codiceVeicolo;

        @Override
        public Integer call() {
            try {
                servizio.sbloccaVeicolo(codiceVeicolo, idUtente);
                System.out.println("[SUCCESSO]: Il veicolo " + codiceVeicolo + " è stato sbloccato con successo. Buona corsa!");
                return 0;
            } catch (RossiGoException e) {
                System.out.println("[ERRORE]: Errore generico di sblocco: " + e.getMessage());
                return 1;
            }
        }
    }

    // termina
    @CommandLine.Command(name = "termina", description = "Termina il noleggio attivo di un veicolo")
    static class TerminaCommand implements Callable<Integer> {

        @CommandLine.Option(names = {"-m", "--minuti"}, required = true, description = "Minuti totali di utilizzo del mezzo")
        private int minuti;

        @CommandLine.Parameters(index = "0", description = "Codice del veicolo da bloccare")
        private String codiceVeicolo;

        @CommandLine.Parameters(index = "1", description = "Codice dell'utente")
        private String idUtente;

        @Override
        public Integer call() {
            try {
                servizio.terminaNoleggio(codiceVeicolo, idUtente, minuti);
                System.out.println("[SUCCESSO]: Corsa terminata per il veicolo " + codiceVeicolo + ".");
                System.out.println("[INFO]: Costo addebitato e livello batteria aggiornato.");
                return 0;
            } catch (Exception e) {
                System.out.println("[ERRORE]: Impossibile terminare il noleggio: " + e.getMessage());
                return 1;
            }
        }
    }

    // simula
    @CommandLine.Command(name = "simula", description = "Avvia la simulazione automatica di una giornata in città (6 scenari)")
    static class SimulaCommand implements Callable<Integer> {

        @Override
        public Integer call() {

            System.out.println("[SCENARIO 1] Tentativo di registrazione di un utente con CF errato...");
            try {
                Utente utenteInvalido = new Utente("U02", "Luca Bianchi", "luca@email.com", "CF_ERRATO_123", 10.0);
                servizio.registraUtente(utenteInvalido);
            } catch (RossiGoException _) {
            }
            System.out.println();

            System.out.println("[SCENARIO 2] L'utente U03 (Anna) prova a sbloccare la bici BK-0042 con 0.50€...");
            try {
                servizio.sbloccaVeicolo("BK-0042", "U03");
            } catch (RossiGoException _) {
            }
            System.out.println();

            System.out.println("[SCENARIO 3] L'utente U01 (Mario) tenta di sbloccare il monopattino SC-0222 scarico...");
            try {
                servizio.sbloccaVeicolo("SC-0222", "U01");
            } catch (RossiGoException _) {
            }
            System.out.println();

            System.out.println("[SCENARIO 4] L'utente U01 (Mario) sblocca il monopattino SC-0107 e fa una corsa di 20 minuti...");
            try {
                servizio.sbloccaVeicolo("SC-0107", "U01");
                System.out.println("   In viaggio...");
                servizio.terminaNoleggio("SC-0107", "U01", 20);
                System.out.println("Corsa terminata! Il costo è stato scalato correttamente in base alla tariffa del monopattino.");
            } catch (RossiGoException _) {
            }
            System.out.println();

            System.out.println("[SCENARIO 5] U01 sblocca la bici BK-0042. Subito dopo, U03 tenta di sbloccare la STESSA bici...");
            try {
                servizio.sbloccaVeicolo("BK-0042", "U01");
                System.out.println("Bici BK-0042 sbloccata da U01 con successo.");

                // Tentativo concorrente di U03 sulla stessa bici
                servizio.sbloccaVeicolo("BK-0042", "U03");
            } catch (RossiGoException _) {
            } finally {
                // pulizia flotta
                try {
                    servizio.terminaNoleggio("BK-0042", "U03", 10);
                } catch (Exception _) {
                }
            }
            System.out.println();

            System.out.println("[SCENARIO 6] Il team di manutenzione mette in ricarica la bici BK-0042...");
            try {
                servizio.mettiInRicarica("BK-0042");
                System.out.println("Veicolo collegato alla stazione Hub. Stato aggiornato a IN_RICARICA.");

                System.out.println("Verifica sicurezza: Un utente prova a sbloccarlo adesso...");
                servizio.sbloccaVeicolo("BK-0042", "U01");
            } catch (RossiGoException _) {
            }

            return 0;
        }
    }
}