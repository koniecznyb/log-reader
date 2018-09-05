package com.ef.database;

import com.ef.AccessRequestEntry;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class DatabaseClient {

    private String url;
    private String user;
    private String password;

    private List<AccessRequestEntry> executeInDatabase(Function<Connection, List<AccessRequestEntry>> action) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            return action.apply(connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void persistLogEntries(List<AccessRequestEntry> logEntries) {
        final String sql = "INSERT INTO logs(ip_address,content,date) "
                + "VALUES(?,?,?)";


        executeInDatabase((connection) -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (AccessRequestEntry accessRequestEntry : logEntries) {
                    preparedStatement.setString(1, accessRequestEntry.getIp());
                    preparedStatement.setString(2, accessRequestEntry.getRequestContent());
                    preparedStatement.setString(3, accessRequestEntry.getTimeOfOccurrence());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<AccessRequestEntry> findRequestsBetween(String startDate, String endDate, int threshold) {
        final String sql = "SELECT ip_address, count(*) AS request_count FROM logs\n" +
                "WHERE date > ? AND date < ?\n" +
                "GROUP BY ip_address\n" +
                "HAVING request_count > ?;";

        return executeInDatabase((connection) -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, startDate);
                preparedStatement.setString(2, endDate);
                preparedStatement.setInt(3, threshold);

                ResultSet resultSet = preparedStatement.executeQuery();

                List<AccessRequestEntry> foundEntries = new ArrayList<>();
                while (resultSet != null && resultSet.next()) {
                    AccessRequestEntry accessRequestEntry = AccessRequestEntry.builder()
                            .ip(resultSet.getString("ip_address"))
                            .occurrenceCount(resultSet.getInt("request_count"))
                            .build();
                    foundEntries.add(accessRequestEntry);
                }
                return foundEntries;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void persistBlockedIpAddresses(List<AccessRequestEntry> blockedIpAddresses) {
        final String sql = "INSERT INTO banned_ips(ip_address,reason) "
                + "VALUES(?,?)";


        executeInDatabase((connection) -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (AccessRequestEntry accessRequestEntry : blockedIpAddresses) {
                    preparedStatement.setString(1, accessRequestEntry.getIp());
                    preparedStatement.setString(2, accessRequestEntry.getBanReason());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
