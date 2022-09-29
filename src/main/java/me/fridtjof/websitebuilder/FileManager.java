package me.fridtjof.websitebuilder;

import me.fridtjof.puddingapi.general.io.Logger;
import org.apache.commons.text.StringSubstitutor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    String baseDir;
    Logger logger;

    Map<String, String> parts = new HashMap<>();

    public FileManager(String baseDirectory, Logger logger) {
        this.baseDir = baseDirectory;
        this.logger = logger;
    }

    public void loopRegisterParts() {

        String path = baseDir  + "/input/parts.txt";

        logger.info("Starting part registration...");

        try {

            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while(line != null) {
                logger.info("Working on page " + baseDir + "/input/" + line);

                parts.put(line, fileToString(line, true));

                line = reader.readLine();
            }

            reader.close();

            logger.info("Done!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loopRegisterPages() {

        String path = baseDir  + "/input/pages.txt";

        logger.info("Starting page registration...");

        try {

            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while(line != null) {
                logger.info("Working on page " + baseDir + "/input/" + line);

                String content = fileToString(line);
                content = formatString(content);
                stringToFile("husqvarna.html", content);

                line = reader.readLine();
            }

            reader.close();

            logger.info("Done!");

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

        path = baseDir  + "/input/" + path;

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

        path = baseDir  + "/output/" + path;

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
