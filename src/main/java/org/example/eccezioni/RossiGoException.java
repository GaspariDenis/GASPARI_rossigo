package org.example.eccezioni;

import org.slf4j.LoggerFactory;

import java.util.logging.Logger;
import java.util.logging.Level;

public class RossiGoException extends Exception{
    public RossiGoException(String msg){
        Logger log = (Logger) LoggerFactory.getLogger("com.example");
        log.setLevel(Level.INFO);
        log.warning(msg);
    }
}
