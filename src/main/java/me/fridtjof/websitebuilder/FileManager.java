package me.fridtjof.websitebuilder;

import me.fridtjof.puddingapi.general.io.Config;
import me.fridtjof.puddingapi.general.io.FileAPI;
import me.fridtjof.puddingapi.general.io.Logger;
import org.apache.commons.text.StringSubstitutor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    WebsiteBuilder wB;
    String inputDir, outputDir;
    public Logger logger;

    Map<String, String> parts = new HashMap<>();

    public FileManager(WebsiteBuilder wB, String baseDirectory) {
        this.wB = wB;
        this.logger = wB.getLogger();
        this.inputDir = wB.inputDir;
        this.outputDir = wB.outputDir;
    }


    //preparation

    void setPaths() {

        inputDir = wB.config.getString("input-directory");
        if(!wB.config.getBoolean("external-input")) {
            inputDir = wB.path + inputDir;
        }

        outputDir = wB.config.getString("output-directory");
        if(!wB.config.getBoolean("external-output")) {
            outputDir = wB.path + outputDir;
        }
    }
    void createDirectories() {
        FileAPI fileAPI = new FileAPI(logger);
        fileAPI.createDirectories(inputDir);
        fileAPI.createDirectories(outputDir);

    }

    void createFiles() {
        try {
            createFile(new File(inputDir + "/pages.txt"));
            createFile(new File(inputDir + "/parts.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void createFile(File file) throws IOException {
        if (file.createNewFile()) {
            logger.info("Creating " + file.getPath());
            return;
        }
        logger.info(file.getPath() + " already exists");
    }

    void createConfig() {
        wB.config = new Config(wB.path + "/", "config");
        wB.config.setDefault("external-input", false);
        wB.config.setDefault("external-output", false);
        wB.config.setDefault("input-directory", "/input");
        wB.config.setDefault("output-directory", "/output");
    }

    //logic
    public void loopRegisterParts() {

        int partCount = 0;

        String path = inputDir + "/parts.txt";

        logger.info("Starting part registration...");

        try {

            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while(line != null) {
                partCount++;

                logger.info("Working on page " + inputDir + "/parts/" + line);

                parts.put(line, fileToString(line, true));

                line = reader.readLine();
            }

            reader.close();

            logger.info(partCount + " part(s) done!");
            logger.info("-----------");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loopRegisterPages() {

        int pageCount = 0;

        String path = inputDir + "/pages.txt";

        logger.info("Starting page registration...");

        try {

            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while(line != null) {
                pageCount++;

                logger.info("Working on page " + outputDir + "/" + line);

                String content = fileToString(line);
                content = formatString(content);
                stringToFile("husqvarna.html", content);

                line = reader.readLine();
            }

            reader.close();

            logger.info(pageCount + " page(s) done!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String fileToString(String path) {
        return fileToString(path, false);
    }

    public String fileToString(String path, boolean part) {

        if(part) {
            path = "parts/" + path;
        }

        path = inputDir + "/" + path;

        logger.info("Trying to read " + path);

        String content = "";

        try {

            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while(line != null) {
                content = content + line + System.lineSeparator();
                line = reader.readLine();
            }

            reader.close();

        } catch (IOException e) {
            content = null;
            throw new RuntimeException(e);
        }

        return content;
    }

    public void stringToFile(String path, String content) {

        path = outputDir + "/" + path;

        logger.info("Trying to print " + path);

        try {
            FileWriter writer = null;
            writer = new FileWriter(new File(path));
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String formatString(String content) {

        logger.info("Formatting...");
        return content = StringSubstitutor.replace(content, parts, "%", "%");
    }
}
