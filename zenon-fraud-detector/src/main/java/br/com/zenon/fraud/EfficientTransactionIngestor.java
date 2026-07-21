package br.com.zenon.fraud;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    public static final int MAX_RECORDS_TO_PROCESS = 10_000;
    private final Semaphore dbSemaphore = new Semaphore(100);

    @Deprecated
    public List<Transaction> readFile(String fileName) {
        List<Transaction> transactions = new ArrayList<>();
        try (
                FileInputStream file = new FileInputStream(fileName);

                Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine() && lineNumber < 1002) {
                lineNumber++;
                String line = scanner.nextLine();

                if (lineNumber < 2) {
                    continue;
                }
                transactions.add(getTransaction(line).get());


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public void readFileNew(String fileName, Consumer<Transaction> consumer) {
        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            lines
                    .skip(1)
                    //.limit(MAX_RECORDS_TO_PROCESS)
                    .map(this::getTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readAsBatchFileNew(String fileName, Consumer<List<Transaction>> consumer) {
        Path path = Paths.get(fileName);
        try (ExecutorService exe = Executors.newVirtualThreadPerTaskExecutor()) {
            try (Stream<String> lines = Files.lines(path)
            ) {
                Iterator<String> iterator = lines.iterator();

                if (iterator.hasNext()) {
                    iterator.next();
                }
                List<String> linesBatch = new ArrayList<>();

                while (iterator.hasNext()) {
                    String line = iterator.next();
                    linesBatch.add(line);

                    if (linesBatch.size() == 4000) {
                        List<String> currentLineBatcher = List.copyOf(linesBatch);
                        exe.submit(() -> executeBatch(currentLineBatcher, consumer));
                        linesBatch.clear();
                    }

                }
                if (!linesBatch.isEmpty()) {
                    List<String> currentLineBatcher = List.copyOf(linesBatch);
                    exe.submit(() -> executeBatch(currentLineBatcher, consumer));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void executeBatch(List<String> linesBatch, Consumer<List<Transaction>> consumer) {

        try {
            dbSemaphore.acquire();
            consumer.accept(linesBatch.stream()
                    .map(this::getTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            dbSemaphore.release();
        }
    }

    private Optional<Transaction> getTransaction(String line) {
        String[] chunk = line.split(",");
        Optional<Transaction> retorno = Optional.empty();

        try {
            retorno = Optional.of(new Transaction(Integer.parseInt(chunk[0]),
                    TransactionType.valueOf(chunk[1]),
                    getBigDecimal(chunk[2]),
                    new TransactionCustomer(chunk[3],
                            getBigDecimal(chunk[4]),
                            getBigDecimal(chunk[5])),
                    new TransactionCustomer(chunk[6],
                            getBigDecimal(chunk[7]),
                            getBigDecimal(chunk[8])),
                    "1".equals(chunk[9]),
                    "1".equals(chunk[10])));

        } catch (Exception e) {
            IO.println(e.getMessage());
        }
        return retorno;
    }

    protected static BigDecimal getBigDecimal(String chunk) {
        if (chunk == null || chunk.trim()
                .isEmpty()) {
            throw new RuntimeException("Valores não podem nullos ou vazios");
        }
        try {
            return new BigDecimal(chunk);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Valores da transaćão teem de ser números");
        }

    }
}
