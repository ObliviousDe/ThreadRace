//Alin & Jothan
// 12/4/25

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class FileTask implements Callable<Result> {

    private final String filePath;

    FileTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Result call() {

        long start = System.currentTimeMillis();
        int charCount = 0;
        int wordCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                charCount += line.length();

                if (!line.trim().isEmpty()) {
                    String[] words = line.trim().split("\\s+");
                    wordCount += words.length;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("Finished reading: " + filePath);
        System.out.println("Characters counted: " + charCount);
        System.out.println("Words counted: " + wordCount);
        System.out.println("Time for " + filePath + ": " + (end - start) + " ms\n");

        return new Result(charCount, wordCount, end - start);
    }
}

class Result {
    int characters;
    int words;
    long time;

    Result(int characters, int words, long time) {
        this.characters = characters;
        this.words = words;
        this.time = time;
    }
}

public class Threadmain {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        long totalStart = System.currentTimeMillis();

        Future<Result> f1 = executor.submit(new FileTask("wonderland2.txt"));
        Future<Result> f2 = executor.submit(new FileTask("shakespere2.txt"));

        try {
            Result r1 = f1.get();
            Result r2 = f2.get();

            long totalEnd = System.currentTimeMillis();

            System.out.println("------------------------------------");
            System.out.println("TOTAL MULTI THREAD TIME: " + (totalEnd - totalStart) + " ms");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
