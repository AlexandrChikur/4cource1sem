package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class FileReaderThread extends Thread {
    private FilesQueue<String> q;

    public FileReaderThread(FilesQueue<String> q) {
        super("thread-file-reader");
        this.q = q;
    }

    @Override
    public void run() {
        JSONParser parser = new JSONParser();

        while (true) {
            if (this.q.isEmpty()) {
                System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tQueue is empty. Sleeping 5s...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                var timeToSleep = (int) ((Math.random() * (5000 - 1000)) + 1000);
                Thread.sleep(timeToSleep);
                String full_path = this.q.pop();
                try (FileReader reader = new FileReader(full_path)) {

                    JSONObject rootJsonObject = (org.json.simple.JSONObject) parser.parse(reader);
                    System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tReading file: " + full_path );
                    System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tFile Num: " + rootJsonObject.get("file_num"));


                } catch (IOException ex) {

                    System.out.println(ex.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
