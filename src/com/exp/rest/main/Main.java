package com.exp.rest.main;

import static com.exp.rest.ws.Constants.IP_ADDRESS;
import static com.exp.rest.ws.Constants.PORT;

import com.exp.rest.ws.ControllerBuilder;
import com.exp.rest.ws.controller.ExperienceSamplingController;
import com.exp.rest.ws.services.impl.ExperienceSamplingService;

import spark.Spark;

public class Main {

	public static void main(final String... args) {

		Spark.port(new Integer(PORT));
		Spark.ipAddress(IP_ADDRESS);
	
		// Building university controller
		final ControllerBuilder<ExperienceSamplingController, ExperienceSamplingService> universityBuilder = 
				new ControllerBuilder<ExperienceSamplingController, ExperienceSamplingService>(ExperienceSamplingController.class, ExperienceSamplingService.class);
		
		universityBuilder.buildController().apply(universityBuilder.buildService());
	}
}