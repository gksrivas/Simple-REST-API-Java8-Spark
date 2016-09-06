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
				+ " ( "
				+ " PARTICIPANT_ID, APP_LABEL, PACKAGE_NAME, APP_VERSION_CODE, HOST, PATH, FILTERED_DATA, TYPE_OF_DATA, DUMMY_DATA_GOOD, APP_WORKS_POST_FILTERING "
				+ " ) "
				+ " VALUES "
				+ " (?,?,?,?,?,?,?,?,?,?) ";			

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(DB_URL, MYSQL_USER, MYSQL_PASSWORD);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		try {

			PreparedStatement pst = connection.prepareStatement(insertTableSQL);
			
			pst.setString(1, experienceSampleSubmitted.getParticipant_id());//Mandatory Fields
			pst.setString(2, experienceSampleSubmitted.getApp_label());
			pst.setString(3, experienceSampleSubmitted.getPackage_name());//Mandatory Fields
			pst.setString(4, experienceSampleSubmitted.getApp_versioncode());
			pst.setString(5, experienceSampleSubmitted.getApp_host());
			pst.setString(6, experienceSampleSubmitted.getApp_hostpath());
			pst.setString(7, experienceSampleSubmitted.getDummy_value());//Mandatory Fields
			pst.setString(8, experienceSampleSubmitted.getTypeof_filtereddata());
			pst.setString(9, experienceSampleSubmitted.getFiltering_ok());
			pst.setString(10, experienceSampleSubmitted.getFilteredapp_worksok());
			
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
