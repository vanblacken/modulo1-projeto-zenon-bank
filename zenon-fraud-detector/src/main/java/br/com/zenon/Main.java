package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.util.List;

public class Main
	{
		void main()
			{
				TransactionIngestor ingestor     = new TransactionIngestor();
				List<Transaction>   transactions = ingestor.readFileNew("data/PS_20174392719_1491204439457_log.csv");
				transactions.stream()
				            .limit(10)
				            .forEach(IO::println);
			}
	}
