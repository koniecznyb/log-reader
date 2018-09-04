package com.ef.database;

import com.ef.LogEntry;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public class DatabaseClient {

    private String url;
    private String user;
    private String password;

    private void executeInDatabase(Consumer<Connection> action) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            action.accept(connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void persistLogEntries(List<LogEntry> logEntries) {
        final String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";
        final String sql = "INSERT INTO logs(ip_address,content,date) "
                + "VALUES(?,?,?)";


        executeInDatabase((connection) -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (LogEntry logEntry : logEntries) {
                    preparedStatement.setString(1, logEntry.getIp());
                    preparedStatement.setString(2, logEntry.getRequestContent());
                    preparedStatement.setString(3, logEntry.getTimeOfOccurrence()
                            .format(DateTimeFormatter.ofPattern(datePattern)));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
