package com.exp.rest.ws.services.impl;

import static com.exp.rest.ws.Constants.DB_URL;
import static com.exp.rest.ws.Constants.MYSQL_PASSWORD;
import static com.exp.rest.ws.Constants.MYSQL_USER;

import com.exp.rest.ws.model.ExperienceSample;
import com.exp.rest.ws.services.IExperienceSamplingService;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ExperienceSamplingService implements IExperienceSamplingService {

	@Override
	public int submitExperienceSample(String requestBody) {

		System.out.print("Submiting Experience Sample \n, Request well formed: " + requestBody );

		Gson data =  new Gson();

		ExperienceSample experienceSampleSubmitted = data.fromJson(requestBody, ExperienceSample.class);

		System.out.println(experienceSampleSubmitted.getPackage_name());

		String insertTableSQL = "INSERT INTO EXP_SAMPLE "
				+ " (PARTICIPANT_ID, PACKAGE_NAME) "
				+ " VALUES "
				+ " (?,?) ";			

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(DB_URL, MYSQL_USER, MYSQL_PASSWORD);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		try {
			Date now = new Date();
			PreparedStatement pst = connection.prepareStatement(insertTableSQL);
			pst.setString(1, now.getTime()+"");
			pst.setString(2, experienceSampleSubmitted.getPackage_name());
			
			pst .executeUpdate();

			/**
			 * Close DB connection
			 */
			connection.close();

		} catch (SQLException e2) {
			e2.printStackTrace();
			return -1;
		}
		return 0;
	}

}
