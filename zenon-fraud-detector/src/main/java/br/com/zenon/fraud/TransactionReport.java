package br.com.zenon.fraud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static br.com.zenon.fraud.TransactionIngestor.getBigDecimal;

public class TransactionReport
	{
		public Statitics generate(String fileName)
			{
				Path path = Paths.get(fileName);
				try (Stream<String> lines = Files.lines(path)) {
					return lines.skip(1)
					            .map(this::getTransaction)
					            .filter(Optional::isPresent)
					            .map(Optional::get)
					            .reduce(Statitics.ZERO, Statitics::addReportTransaction, Statitics::add);
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
					                                      null,
					                                      getBigDecimal(chunk[2]),
					                                      null,
					                                      null,
					                                      "1".equals(chunk[9]),
					                                      false));

				} catch (Exception e) {
					IO.println(e.getMessage());
				}
				return retorno;
			}
	}
