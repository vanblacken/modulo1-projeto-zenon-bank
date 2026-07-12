package br.com.zenon.fraud;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain
	{
		void main(String[] args)
			{

				String language = args.length > 0 ? args[0] : "pt";
				var transactionReport = new TransactionReport();


				Statitics statitics = transactionReport.generate("data/PS_20174392719_1491204439457_log.csv");

				Locale       locale         = Locale.of(language);
				var          resourceBundle = ResourceBundle.getBundle("report", locale);
				NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
				currencyFormat.setCurrency(Currency.getInstance("USD"));
				NumberFormat internationalFormat = NumberFormat.getIntegerInstance(locale);

				String formattedTotalTransactions = internationalFormat.format(statitics.totalTransactions());
				String formattedTotalFrauds       = internationalFormat.format(statitics.totalFrauds());
				String formattedTotalValue        = currencyFormat.format(statitics.totalAmount());

				String msgTotalTransaction = resourceBundle.getString("label.total.transactions");
				String msgTotalFrauds      = resourceBundle.getString("label.total.frauds");
				String msgTotalValue       = resourceBundle.getString("label.total.value");

				IO.println("""
				           %s: %s
				           %s: %s
				           %s: %s """
										 .formatted(msgTotalTransaction,
					                      formattedTotalTransactions,
					                      msgTotalFrauds,
					                      formattedTotalFrauds,
					                      msgTotalValue,
					                      formattedTotalValue));
			}
	}
