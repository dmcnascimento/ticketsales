package br.com.ufs.ds3;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.exception.TicketSalesException;

public class Main {
	public static void main(String[] args) {
		loadDatabase();
	}

	private static void loadDatabase() {
		try (Connection con = DB.getConnection()) {
			Statement st = con.createStatement();
			Scanner scanner = new Scanner(Main.class.getResourceAsStream("/initial_data.sql"));
			StringBuilder sql = new StringBuilder();
			while (scanner.hasNextLine()) {
				sql.append(scanner.nextLine());
			}
			scanner.close();
			st.execute(sql.toString());
			con.commit();
		} catch (SQLException e) {
			throw new TicketSalesException(e);
		}
	}
}
