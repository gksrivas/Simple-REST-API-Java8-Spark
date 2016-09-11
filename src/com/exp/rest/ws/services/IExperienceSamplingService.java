package com.exp.rest.ws.services;

public interface IExperienceSamplingService extends IAbstractService {
	public int submitExperienceSample(final String requestBody);
	
	public int submitAppDetailsTelemetry(final String requestBody);
	
	public String updateFromPrivacyProxySignatures(final String updateKey);
}
