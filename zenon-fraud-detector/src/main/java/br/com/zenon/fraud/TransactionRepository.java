package br.com.zenon.fraud;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
	{
		Optional<Transaction> findByOriginalName(String orignName);
		void save(Transaction transaction);

		void saveAll(List<Transaction> transactions);
	}
