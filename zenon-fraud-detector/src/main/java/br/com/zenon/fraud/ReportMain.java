package br.com.zenon.fraud;

public class ReportMain
	{
		void main()
			{
				var transactionReport = new TransactionReport();

				Statitics statitics = transactionReport.generate("data/PS_20174392719_1491204439457_log.csv");
				IO.println("""
				           Total de Linhas: %d /n Total de Fraudes: %d /n Total de Valor: %.2f"""
				  .formatted(statitics.totalTransactions(), statitics.totalFrauds(), statitics.totalAmount()));
			}
	}
