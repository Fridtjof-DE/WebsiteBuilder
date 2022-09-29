package me.fridtjof.websitebuilder;

import java.io.*;

import me.fridtjof.puddingapi.general.io.Config;
import me.fridtjof.puddingapi.general.io.Logger;

public class WebsiteBuilder {

    Logger logger;
    String path;
    Config config;
    FileManager fileManager;

    public static void main(String[] args) {
        System.out.println("Starting...");
        new WebsiteBuilder();
    }

    private WebsiteBuilder() {
        logger = new Logger(true);
        logger.info("Started!");

        path = System.getProperty("user.dir");

        fileManager = new FileManager(path, logger);

        fileManager.loopRegisterParts();
        fileManager.loopRegisterPages();
    }

    //getters

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }
}
