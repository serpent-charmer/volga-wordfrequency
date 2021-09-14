package com.volga.wordstats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordCacheReader extends WordReader {
	
	WordCacheReader(String path) {
		super(path);
	}

	public void read() {
		try (Connection con = DriverManager.getConnection("jdbc:sqlite:sample.db")) {

			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);

			
			String filehash = Utils.getMd5Hash(this.path);
			PreparedStatement query = con.prepareStatement("select * from wordstat where filehash = ? order by frequency desc;");
			
			query.setString(1, filehash);
			
			ResultSet rs = query.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("word") + " " + rs.getInt("frequency"));
				LoggerInstance.logger.info(rs.getString("word") + " " + rs.getInt("frequency"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LoggerInstance.logger.catching(e);
		}
	}
}