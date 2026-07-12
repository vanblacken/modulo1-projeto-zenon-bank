package br.com.zenon;

import br.com.zenon.fraud.FraudAnalizer;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.util.List;
import java.util.Optional;

public class Main
	{
		void main()
			{
				TransactionIngestor ingestor     = new TransactionIngestor();
				List<Transaction>   transactions = ingestor.readFileNew("data/PS_20174392719_1491204439457_log.csv");
				transactions.forEach(IO::println);

				var fraudAnalizer = new FraudAnalizer(transactions);
				IO.println("Total de Fraudes: " + fraudAnalizer.countFrauds());
				fraudAnalizer.findHighestValueFrauds(3)
				             .forEach(n -> IO.println("3 Fraudes de mAior Valor: " + n.amount()
				                                                                      .toPlainString()));
				fraudAnalizer.findTopSuspciousClients(5)
				             .forEach(n -> IO.println("top 5 clientes suspeitos: " + n));
				IO.println("Valor Total de Fraudes: " + fraudAnalizer.totalFraudsValue()
				                                                     .toPlainString());
				IO.println("Quantidade Total de Fraudes por tipo : " + fraudAnalizer.countFraudsByType());
			}
	}
