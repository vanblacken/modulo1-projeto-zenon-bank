package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.util.List;
import java.util.Optional;

public class Main
	{
		void main()
			{
				TransactionIngestor         ingestor     = new TransactionIngestor();
				List<Transaction> transactions = ingestor.readFileNew("data/paysim_with_bad_data.txt");
				transactions.stream()
				            .limit(10)
				            .forEach(IO::println);
			}
	}
