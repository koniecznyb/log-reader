package com.ef.database;

import com.ef.AccessRequestEntry;
import com.ef.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseClientTest {

    @InjectMocks
    private DatabaseClient databaseClient;

    @Mock
    private DriverManagerWrapper driverManagerWrapper;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Before
    public void setUp() throws Exception {
        when(driverManagerWrapper.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
    }

    @Test
    public void shouldPersistLogEntries() throws Exception {
//        given
        List<AccessRequestEntry> accessRequestEntries = TestUtils.buildAccessRequestEntryList();

//        when
        databaseClient.persistLogEntries(accessRequestEntries);

//        then
        verify(driverManagerWrapper, only()).getConnection();
        verify(preparedStatement, times(1)).executeBatch();
        verify(preparedStatement, times(2)).addBatch();

        verify(preparedStatement, times(1)).setString(1, "192.168.0.1");
        verify(preparedStatement, times(1)).setString(2, "GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        verify(preparedStatement, times(1)).setString(3, "2017-01-01 05:38:36.508");

        verify(preparedStatement, times(1)).setString(1, "192.168.0.3");
        verify(preparedStatement, times(1)).setString(2, "GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (" +
                "KHTML, like Gecko) Chrome/60.0.3112.116 Mobile Safari/537.36");
        verify(preparedStatement, times(1)).setString(3, "2017-03-01 04:38:36.508");
    }

    @Test
    public void shouldFindRequestsBetween() throws SQLException {
        //        given
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("ip_address")).thenReturn("192.168.0.1");
        when(resultSet.getInt("request_count")).thenReturn(200);

//        when
        List<AccessRequestEntry> requestsBetween =
                databaseClient.findRequestsBetween("2017-01-01.13:00:00", "2017-01-01.14:00:00", 200);

//        then
        assertEquals(1, requestsBetween.size());
        AccessRequestEntry request = requestsBetween.get(0);
        assertEquals("192.168.0.1", request.getIp());
        assertEquals(200, (int) request.getOccurrenceCount());
        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement, times(1)).setString(1, "2017-01-01.13:00:00");
        verify(preparedStatement, times(1)).setString(2, "2017-01-01.14:00:00");
        verify(preparedStatement, times(1)).setInt(3, 200);
    }

    @Test
    public void shouldPersistBlockedIpAddresses() throws SQLException {
        //        given
        List<AccessRequestEntry> accessRequestEntries = TestUtils.buildAccessRequestEntryList();

//        when
        databaseClient.persistBlockedIpAddresses(accessRequestEntries);

//        then
        //        then
        verify(driverManagerWrapper, only()).getConnection();
        verify(preparedStatement, times(1)).executeBatch();
        verify(preparedStatement, times(2)).addBatch();

        verify(preparedStatement, times(1)).setString(1, "192.168.0.1");
        verify(preparedStatement, times(1)).setString(2, "reason1");

        verify(preparedStatement, times(1)).setString(1, "192.168.0.3");
        verify(preparedStatement, times(1)).setString(2, "reason2");
    }
}