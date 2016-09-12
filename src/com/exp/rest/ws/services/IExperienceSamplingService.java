package com.exp.rest.ws.services;

import com.exp.rest.ws.model.AppDetailsTelemetry;

public interface IExperienceSamplingService extends IAbstractService {
	public int submitExperienceSample(final String requestBody);
	
	public int submitAppDetailsTelemetry(final String requestBody);
	
	public AppDetailsTelemetry[] getServerWhitelist();
	
	public String updateFromPrivacyProxySignatures(final String updateKey);
}
