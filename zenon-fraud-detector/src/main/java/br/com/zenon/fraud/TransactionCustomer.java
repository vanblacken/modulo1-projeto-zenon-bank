package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionCustomer(String name,
                                  BigDecimal oldBalance,
                                  BigDecimal newBalance)
	{
		public TransactionCustomer
		{
			if (name == null)
				throw new IllegalArgumentException("Name não pode ser nulo");
			if (oldBalance.signum()<=0)
				throw new IllegalArgumentException("OldBalance não pode ser negativo ou zero");
			if (newBalance.signum()<=0)
				throw new IllegalArgumentException("NewBalance não pode ser negativo ou zero");
		}

	}
