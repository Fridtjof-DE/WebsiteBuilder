package me.fridtjof.websitebuilder;

import java.io.*;

import me.fridtjof.puddingapi.general.io.Config;
import me.fridtjof.puddingapi.general.io.FileAPI;
import me.fridtjof.puddingapi.general.io.Logger;

public class WebsiteBuilder {

    long startTime = System.currentTimeMillis();
    private Logger logger;
    String path, inputDir, outputDir;
    Config config;
    FileManager fM;

    public static void main(String[] args) {
        System.out.println("Starting...");
        new WebsiteBuilder();
    }

    private WebsiteBuilder() {
        logger = new Logger(false);

        path = System.getProperty("user.dir");

        fM = new FileManager(this, path);

        fM.createConfig();
        fM.setPaths();
        fM.createDirectories();
        fM.createFiles();

        logger.info("Started!");

        fM.loopRegisterParts();
        fM.loopRegisterPages();

        long processTime = System.currentTimeMillis() - startTime;
        logger.info("Website generated in " + processTime + "ms!");
        logger.info("Ending...");
    }

    //getters

    public Logger getLogger() {
        return logger;
    }
}
