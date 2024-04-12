package br.com.magna.caca_palavra.services;

import java.util.logging.Logger;

public class LogHandler {

	private static final Logger logger = Logger.getLogger(LogHandler.class.getName());

    public static void Info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.severe(message);
    }
}
