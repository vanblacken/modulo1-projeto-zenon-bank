package br.com.zenon.fraud;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionIngestor
	{
		@Deprecated
		public List<Transaction> readFile(String fileName)
			{
				List<Transaction> transactions = new ArrayList<>();
				try (
					FileInputStream file = new FileInputStream(fileName);

					Scanner scanner = new Scanner(file)) {
					int lineNumber = 0;
					while (scanner.hasNextLine() && lineNumber < 1002) {
						lineNumber++;
						String line = scanner.nextLine();

						if (lineNumber < 2) {
							continue;
						}

						String[]    chunc       = line.split(",");
						Transaction transaction = getTransaction(chunc);
						transactions.add(transaction);


					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return transactions;
			}

		public List<Transaction> readFileNew(String fileName)
			{
				List<Transaction> transactions = new ArrayList<>();
				Path              path         = Paths.get(fileName);
				try {
					Files.readAllLines(path)
					     .stream()
					     .skip(1)
					     .limit(1000)
					     .forEach(line ->
													{
														String[]    chunc       = line.split(",");
														Transaction transaction = getTransaction(chunc);
														transactions.add(transaction);
													});
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				return transactions;
			}

		private Transaction getTransaction(String[] chunc)
			{
				return new Transaction(Integer.parseInt(chunc[0]),
				                       TransactionType.valueOf(chunc[1]),
				                       new BigDecimal(chunc[2]),
				                       new TransactionCustomer(chunc[3],
				                                               new BigDecimal(chunc[4]),
				                                               new BigDecimal(chunc[5])),
				                       new TransactionCustomer(chunc[6],
				                                               new BigDecimal(chunc[7]),
				                                               new BigDecimal(chunc[8])),
				                       Boolean.parseBoolean(chunc[9]),
				                       Boolean.parseBoolean(chunc[10]));
			}
	}
