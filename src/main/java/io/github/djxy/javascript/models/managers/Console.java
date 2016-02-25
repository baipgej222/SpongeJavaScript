package io.github.djxy.javascript.models.managers;

/**
 * Created by Samuel on 2016-02-21.
 */
public class Console {

    private final org.slf4j.Logger logger;

    public Console(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void log(Object object){
        logger.info(object.toString());
    }
}
