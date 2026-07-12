package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Statitics(long totalTransactions,
                        long totalFrauds,
                        BigDecimal totalAmount)
	{
		final static Statitics ZERO = new Statitics(0, 0, BigDecimal.ZERO);

		Statitics addReportTransaction(Transaction reportTransaction)
			{
				return new Statitics(totalTransactions() + 1,
				                     totalFrauds() + (reportTransaction.isFraud() ? 1 : 0),
				                     totalAmount()
															 .add(reportTransaction.amount()));
			}

		Statitics add(Statitics reportTransaction)
			{
				return new Statitics(totalTransactions() + reportTransaction.totalTransactions(),
				                     totalFrauds()
				                     + (totalFrauds() > 0 ? totalFrauds()
				                                            + reportTransaction.totalFrauds() : 0),
				                     totalAmount()
															 .add(reportTransaction.totalAmount()));
			}
	}
