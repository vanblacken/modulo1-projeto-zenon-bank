package br.com.zenon.fraud;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TransactionIngestor
	{

		public static final int MAX_RECORDS_TO_PROCESS = 10000;

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
						transactions.add(getTransaction(line).get());


					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return transactions;
			}

		public List<Transaction> readFileNew(String fileName)
			{
				Path path = Paths.get(fileName);
				try {
					return Files.readAllLines(path)
					            .stream()
					            .skip(1)
					            .limit(MAX_RECORDS_TO_PROCESS)
					            .map(this::getTransaction)
					            .filter(Optional::isPresent)
					            .map(Optional::get)
					            .toList();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		private Optional<Transaction> getTransaction(String line)
			{
				String[]              chunk   = line.split(",");
				Optional<Transaction> retorno = Optional.empty();

				try {
					retorno = Optional.of(new Transaction(Integer.parseInt(chunk[0]),
					                                      TransactionType.valueOf(chunk[1]),
					                                      getBigDecimal(chunk[2]),
					                                      new TransactionCustomer(chunk[3],
					                                                              getBigDecimal(chunk[4]),
					                                                              getBigDecimal(chunk[5])),
					                                      new TransactionCustomer(chunk[6],
					                                                              getBigDecimal(chunk[7]),
					                                                              getBigDecimal(chunk[8])),
					                                      "1".equals(chunk[9]),
					                                      "1".equals(chunk[10])));

				} catch (Exception e) {
					IO.println(e.getMessage());
				}
				return retorno;
			}

		protected static BigDecimal getBigDecimal(String chunk)
			{
				if (chunk == null || chunk.trim()
				                          .isEmpty()) {
					throw new RuntimeException("Valores não podem nullos ou vazios");
				}
				try {
					return new BigDecimal(chunk);
				} catch (NumberFormatException e) {
					throw new RuntimeException("Valores da transaćão teem de ser números");
				}

			}
	}
