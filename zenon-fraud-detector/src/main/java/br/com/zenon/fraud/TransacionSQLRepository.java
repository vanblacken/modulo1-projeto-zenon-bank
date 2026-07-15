package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransacionSQLRepository implements TransactionRepository {

    @Override
    public Optional<Transaction> findByOriginalName(String orignName) {

        String sql = """ 
                SELECT id, step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_recipient, 
                       old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud
                FROM zenon_frauds.transactions
                WHERE name_origin = ? 
                ORDER BY step
                LIMIT 1
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, orignName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    IO.println(resultSet.getString("name_origin"));
                    Transaction t = mapResultSetToTransaction(resultSet);
                    return Optional.of(t);
                } else {
                    IO.println("No transaction found to:" + orignName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (step, type, amount, name_origin, new_balance_origin, old_balance_origin," +
                " name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transaction.step());
            preparedStatement.setString(2, transaction.type().name());
            preparedStatement.setBigDecimal(3, transaction.amount());
            preparedStatement.setString(4, transaction.origin().name());
            preparedStatement.setBigDecimal(5, transaction.origin().oldBalance());
            preparedStatement.setBigDecimal(6, transaction.origin().newBalance());
            preparedStatement.setString(7, transaction.recipient().name());
            preparedStatement.setBigDecimal(8, transaction.recipient().oldBalance());
            preparedStatement.setBigDecimal(9, transaction.recipient().newBalance());
            preparedStatement.setBoolean(10, transaction.isFraud());
            preparedStatement.setBoolean(11, transaction.isFlaggedFraud());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Transaction mapResultSetToTransaction(ResultSet rs) {

        try {
            int step = rs.getInt("step");
            TransactionType type = TransactionType.valueOf(rs.getString("type"));
            BigDecimal amount = rs.getBigDecimal("amount");
            TransactionCustomer customerOrigin = new TransactionCustomer(
                    rs.getString("name_origin"),
                    rs.getBigDecimal("new_balance_origin"),
                    rs.getBigDecimal("old_balance_origin"));
            TransactionCustomer customerRecipient = new TransactionCustomer
                    (rs.getString("name_recipient"),
                            rs.getBigDecimal("old_balance_recipient"),
                            rs.getBigDecimal("new_balance_recipient"));
            boolean isFraud = rs.getBoolean("is_fraud");
            boolean is_flagged_fraud = rs.getBoolean("is_flagged_fraud");
            return new Transaction(step, type, amount, customerOrigin, customerRecipient, isFraud, is_flagged_fraud);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
