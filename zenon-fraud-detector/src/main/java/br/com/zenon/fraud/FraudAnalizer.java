package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FraudAnalizer
	{

		private final List<Transaction> transactions;

		public FraudAnalizer(List<Transaction> transactions)
			{
				Objects.requireNonNull(transactions);
				this.transactions = transactions;
			}

		public long countFrauds()
			{
				return transactions.stream()
				                   .filter(Transaction::isFraud)
				                   .count();
			}

		public List<Transaction> findHighestValueFrauds(int i)
			{
				return transactions.stream()
				                   .filter(Transaction::isFraud)
				                   .sorted(Comparator.comparing(Transaction::amount)
				                                     .reversed())
				                   .limit(i)
				                   .toList();

			}

		public List<String> findTopSuspciousClients(int i)
			{
				return transactions.stream()
				                   .filter(Transaction::isFraud)
				                   .sorted(Comparator.comparing(Transaction::amount)
				                                     .reversed())
				                   .map(transaction -> transaction.origin()
				                                                  .name())
				                   .distinct()
				                   .limit(i)
				                   .toList();
			}

		public BigDecimal totalFraudsValue()
			{
				return transactions.stream()
				                   .filter(Transaction::isFraud)
				                   .map(Transaction::amount)
				                   .reduce(BigDecimal.ZERO, BigDecimal::add);
			}

		public List<String> countFraudsByType()
			{
				return transactions.stream()
				                   .filter(Transaction::isFraud)
				                   .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
				                   .entrySet()
				                   .stream()
				                   .map(entry -> entry.getKey() + ": " + entry.getValue())
				                   .toList();
			}
	}
