package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository
        implements TransactionRepository {
    private final Map<String, Transaction> transactionsTransactionMap;


    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactionsTransactionMap = transactions.stream()
                .collect(Collectors.toMap(t -> t.origin()
                                .name(),
                        transaction -> transaction));
    }

    @Override
    public Optional<Transaction> findByOriginalName(String orignName) {
        return Optional.ofNullable(transactionsTransactionMap.get(orignName));
    }

    @Override
    public void save(Transaction transaction) {
        this.transactionsTransactionMap.putIfAbsent(transaction.origin().name(), transaction);
    }

    @Override
    public void saveAll(List<Transaction> transactions) {

    }
}
