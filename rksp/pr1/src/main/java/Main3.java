import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.FileGeneratorThread;
import utils.FileReaderThread;
import utils.FilesQueue;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Main3 {

    public static void main(String[] args) throws InterruptedException {
        FilesQueue<String> filesQueue = new FilesQueue<>(5);
        String path = "C:\\Users\\sasha\\Desktop\\4cource1sem\\pr1\\src\\main\\resources\\jsons";

        FileGeneratorThread fileGeneratorThread = new FileGeneratorThread(path, filesQueue);


        FileReaderThread fileReaderThread = new FileReaderThread(filesQueue);

        fileGeneratorThread.start();
        fileReaderThread.start();

    }

}
