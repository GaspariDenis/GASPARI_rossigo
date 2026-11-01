package org.example.eccezioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

public class RossiGoException extends Exception{
    public RossiGoException(String msg){
        super(msg);
        Logger log = (Logger) LoggerFactory.getLogger("com.example");
        log.warn(msg);
    }
}
