package br.com.ufs.ds3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.ufs.ds3.exception.TicketSalesException;

public final class DB {
	private static final String DB_URL = "jdbc:hsqldb:mem:ticketsalesdb";
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(DB_URL, "SA", "");
		} catch (SQLException e) {
			throw new TicketSalesException(e);
		}
	}
}
