package com.exp.rest.ws.model;

public class ExperienceSample {
	private String participant_id;
	private String app_label;
	private String package_name;
	private String app_versioncode;
	private String app_host;
	private String app_hostpath;
	private String request_type;
	private String typeof_filtereddata;
	private String dummy_value;
	private String filtering_ok;
	private String filteredapp_worksok;
	
	public String getParticipant_id() {
		return participant_id;
	}
	
	public void setParticipant_id(String participant_id) {
		this.participant_id = participant_id;
	}
	
	public String getPackage_name() {
		return package_name;
	}
	
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getApp_label() {
		return app_label;
	}

	public void setApp_label(String app_label) {
		this.app_label = app_label;
	}

	public String getApp_versioncode() {
		return app_versioncode;
	}

	public void setApp_versioncode(String app_versioncode) {
		this.app_versioncode = app_versioncode;
	}

	public String getApp_host() {
		return app_host;
	}

	public void setApp_host(String app_host) {
		this.app_host = app_host;
	}

	public String getApp_hostpath() {
		return app_hostpath;
	}

	public void setApp_hostpath(String app_hostpath) {
		this.app_hostpath = app_hostpath;
	}

	public String getTypeof_filtereddata() {
		return typeof_filtereddata;
	}

	public void setTypeof_filtereddata(String typeof_filtereddata) {
		this.typeof_filtereddata = typeof_filtereddata;
	}

	public String getDummy_value() {
		return dummy_value;
	}

	public void setDummy_value(String dummy_value) {
		this.dummy_value = dummy_value;
	}

	public String getFiltering_ok() {
		return filtering_ok;
	}

	public void setFiltering_ok(String filtering_ok) {
		this.filtering_ok = filtering_ok;
	}

	public String getFilteredapp_worksok() {
		return filteredapp_worksok;
	}

	public void setFilteredapp_worksok(String filteredapp_worksok) {
		this.filteredapp_worksok = filteredapp_worksok;
	}

	public String getRequest_type() {
		return request_type;
	}

	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}
}
