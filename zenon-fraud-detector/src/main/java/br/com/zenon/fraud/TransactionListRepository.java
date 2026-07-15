package br.com.zenon.fraud;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository
	implements TransactionRepository
	{
		private List<Transaction> transactions;

		public TransactionListRepository(List<Transaction> transactions)
			{
				Objects.requireNonNull(transactions);
				this.transactions = transactions;
			}

		@Override
		public Optional<Transaction> findByOriginalName(String orignName)
			{
				return transactions.stream()
				                   .filter(transaction -> transaction.origin()
				                                                     .name()
				                                                     .equals(orignName))
				                   .findFirst();
			}

		@Override
		public void save(Transaction transaction) {
			this.transactions.add(transaction);
		}
	}
