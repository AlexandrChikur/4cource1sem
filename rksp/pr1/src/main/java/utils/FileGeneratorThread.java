package utils;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class FileGeneratorThread extends Thread {
    private String path;
    private FilesQueue<String> q;

    public FileGeneratorThread(String path, FilesQueue<String> q) {
        super("thread-file-generator");
        this.path = path;
        this.q = q;
    }

    @Override
    public void run() {
        int i = 0;

        while (true) {
            if (this.q.isFull()) {
                System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tQueue reached is limit: " + q);
                System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tSleeping for 5s... ");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            var exampleLines = (int) ((Math.random() * (5000 - 1000)) + 1000);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("file_num", exampleLines);
            for (int j = 0; j < exampleLines; j++) {
                jsonObject.put("example" + j, new JSONObject().put("key" + j, "value" + j));
            }
            String jsonString = jsonObject.toString();
            String full_path = this.path + "\\" + exampleLines + ".json";

            try (FileWriter writer = new FileWriter(full_path, false)) {
                q.add(full_path);
                writer.write(jsonString);
                writer.flush();
                System.out.println("[" + Thread.currentThread().getName() + "]" + "\t\tFile '" + full_path + "' written and queued");
                var timeToSleep = (int) ((Math.random() * (500 - 100)) + 100);
                Thread.sleep(timeToSleep);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
