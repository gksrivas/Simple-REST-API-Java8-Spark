package com.exp.rest.ws.services.impl;

import static com.exp.rest.ws.Constants.DB_URL_EXP_SAMPLE;
import static com.exp.rest.ws.Constants.DB_URL_PRIVACYPROXY;
import static com.exp.rest.ws.Constants.MYSQL_PASSWORD;
import static com.exp.rest.ws.Constants.MYSQL_USER;

import com.exp.rest.ws.model.AppDetailsTelemetry;
import com.exp.rest.ws.model.ExperienceSample;
import com.exp.rest.ws.services.IExperienceSamplingService;
import com.exp.rest.ws.util.Sha256;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.Type;

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
			connection = DriverManager.getConnection(DB_URL_EXP_SAMPLE, MYSQL_USER, MYSQL_PASSWORD);

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

	@Override
	public String updateFromPrivacyProxySignatures(String updateKey) {

		String updateTimeStamp = getLastUpdateTime();
		if (updateTimeStamp.equals("-107"))
			return "Unable to Update, Exception Occured.";

		String signatureKeySQL = " SELECT `key` "
				+ " FROM public_signatures ";
		if (updateTimeStamp != null && !updateTimeStamp.equals("")) {
			signatureKeySQL += " WHERE last_update > ? ";
		}

		Connection connection = null;
		ResultSet rs = null;
		HashMap <String, ExperienceSample> keyMapping = new HashMap<>();

		try {
			connection = DriverManager.getConnection(DB_URL_PRIVACYPROXY, MYSQL_USER, MYSQL_PASSWORD);

			PreparedStatement pst = connection.prepareStatement(signatureKeySQL);
			if (updateTimeStamp != null && !updateTimeStamp.equals("")) {
				pst.setString(1, updateTimeStamp);
			}
			rs =  pst.executeQuery();
			while(rs.next()) {
				String key = rs.getString(1);
				System.out.println("Key: " + key);

				String[] keySplit = key.split("_");

				ExperienceSample sample = new ExperienceSample();
				//Example Key HTTP_play.googleapis.com_/log_POST_com.google.android.gsf.login_18

				sample.setApp_host(keySplit[1]);
				String hostPath = "";
				for (int i=2; i < keySplit.length - 3; i++){
					hostPath += keySplit[i];
				}
				sample.setApp_hostpath(hostPath);
				sample.setRequest_type(keySplit[keySplit.length - 3]);
				sample.setPackage_name(keySplit[keySplit.length - 2]);
				sample.setApp_versioncode(keySplit[keySplit.length - 1]);

				keyMapping.put(Sha256.hash256(key), sample);
			}

			insertIntoAppSignatureTbl(keyMapping);

			/**
			 * Close DB connection
			 */
			connection.close();

		} catch (SQLException e2) {
			e2.printStackTrace();
			return "Unable to Update, Exception Occured.";
		}

		return "Update Successful";
	}

	private String insertIntoAppSignatureTbl(HashMap<String, ExperienceSample> keyMapping) {

		String insertTableSQL = "INSERT INTO APP_SIGNATURE "
				+ " ( "
				+ " HASH_SIGNATURE_KEY, PACKAGE_NAME, APP_VERSION_CODE, HOST_URL, HOST_PATH, REQUEST_TYPE "
				+ " ) "
				+ " VALUES "
				+ " (?,?,?,?,?,?) ";			

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(DB_URL_EXP_SAMPLE, MYSQL_USER, MYSQL_PASSWORD);

			PreparedStatement pst = connection.prepareStatement(insertTableSQL);

			Map<String, ExperienceSample> map = keyMapping;
			for(Entry<String, ExperienceSample> e: map.entrySet()){
				ExperienceSample sample = e.getValue();
				pst.setString(1, e.getKey());
				pst.setString(2, sample.getPackage_name());
				pst.setString(3, sample.getApp_versioncode());
				pst.setString(4, sample.getApp_host());
				pst.setString(5, sample.getApp_hostpath());
				pst.setString(6, sample.getRequest_type());
				pst .executeUpdate();
			}

			/**
			 * Close DB connection
			 */
			connection.close();

		} catch (SQLException e2) {
			e2.printStackTrace();
			return "Unable to Update, Exception Occured.";
		}
		return "Success";
	}

	private String getLastUpdateTime() {
		String lastUpdateTime = "";

		String lastUpdateSQL = " SELECT DISTINCT LAST_UPDATE "
				+ " FROM APP_SIGNATURE "
				+ " ORDER BY LAST_UPDATE DESC "
				+ " LIMIT 1 ";

		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = DriverManager.getConnection(DB_URL_EXP_SAMPLE, MYSQL_USER, MYSQL_PASSWORD);

			PreparedStatement pst = connection.prepareStatement(lastUpdateSQL);
			rs =  pst.executeQuery();

			if(rs.next()) {
				lastUpdateTime = rs.getString(1);
			}

			/**
			 * Close DB connection
			 */
			connection.close();

		} catch (SQLException e2) {
			e2.printStackTrace();
			return "-107";
		}
		return lastUpdateTime;
	}

	@Override
	public int submitAppDetailsTelemetry(String requestBody) {

		System.out.print("Submiting App Details Telemetry \n, Request well formed: " + requestBody );

		Gson data =  new Gson();
		try {

			Type type = new TypeToken<List<AppDetailsTelemetry>>(){}.getType();
			List<AppDetailsTelemetry> inpList = data.fromJson(requestBody, type);

			AppDetailsTelemetry[] telemetrySamplesSubmitted = inpList.toArray(new AppDetailsTelemetry[inpList.size()]);


			String insertTableSQL = "REPLACE INTO APP_DETAILS_TELEMETRY "
					+ " ( "
					+ " PARTICIPANT_ID, PACKAGE_NAME, APP_VERSION_CODE, INTERNET_REQUESTS, LEAKY_REQUESTS, REMOTE_HOSTS"
					+ " ) "
					+ " VALUES "
					+ " (?,?,?,?,?,?) ";			

			Connection connection = null;

			connection = DriverManager.getConnection(DB_URL_EXP_SAMPLE, MYSQL_USER, MYSQL_PASSWORD);


			PreparedStatement pst = connection.prepareStatement(insertTableSQL);
			for (AppDetailsTelemetry app :  telemetrySamplesSubmitted) {

				pst.setString(1, app.participant_id);
				pst.setString(2, app.package_name);
				pst.setString(3, app.app_version_code);
				pst.setString(4, app.internet_requests);
				pst.setString(5, app.leaky_requests);
				pst.setString(6, app.remote_hosts);

				pst .executeUpdate();				
			}

			/**
			 * Close DB connection
			 */
			connection.close();
		} catch (Exception e2) {
			e2.printStackTrace();
			return -1;
		}
		return 0;		
	}

	@Override
	public AppDetailsTelemetry[] getServerWhitelist() {

		String serverWhitelistSQL = " SELECT PACKAGE_NAME, APP_VERSION_CODE, count(1) as NUM_USERS, SUM(INTERNET_REQUESTS), SUM(LEAKY_REQUESTS) "
				+ " FROM APP_DETAILS_TELEMETRY "
				+ " GROUP BY PACKAGE_NAME, APP_VERSION_CODE ";

		Connection connection = null;
		ResultSet rs = null;
		ArrayList<AppDetailsTelemetry> whiteList = new ArrayList<>();

		try {
			connection = DriverManager.getConnection(DB_URL_EXP_SAMPLE, MYSQL_USER, MYSQL_PASSWORD);

			PreparedStatement pst = connection.prepareStatement(serverWhitelistSQL);
			rs =  pst.executeQuery();


			while(rs.next()) {
				if(rs.getInt(4) > 50 && rs.getInt(5) <= 5) {

					String packageName = rs.getString(1);
					String appVersionCode = rs.getString(2);
					System.out.println("PackageName: " + packageName + " --> " + appVersionCode);

					AppDetailsTelemetry whiteListApp = new AppDetailsTelemetry();
					whiteListApp.package_name = packageName;
					whiteListApp.app_version_code = appVersionCode;
					whiteListApp.num_users = rs.getString(3);
					whiteList.add(whiteListApp);
				}
			}

			/**
			 * Close DB connection
			 */
			connection.close();

		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}	

		return whiteList.toArray(new AppDetailsTelemetry[whiteList.size()]);
	}

}
