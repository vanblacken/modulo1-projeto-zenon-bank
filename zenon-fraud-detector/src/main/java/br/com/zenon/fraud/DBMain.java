package br.com.zenon.fraud;

import java.util.List;

public class DBMain {
    void main() {

        var transactionIngestor = new EfficientTransactionIngestor();
        long startTime = System.nanoTime();
        TransacionSQLRepository transacionSQLRepository = new TransacionSQLRepository();
        List<Transaction> transactions;
        transactionIngestor.readAsBatchFileNew("data/PS_20174392719_1491204439457_log.csv", transacionSQLRepository::saveAll);


        //transactions.forEach(transacionSQLRepository::save);
        //IO.println("tamanho: " + transactions.size());
        //transacionSQLRepository.saveAll(transactions);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        IO.println("Duration: " + duration / 1000000);
    }
}

