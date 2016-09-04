package com.exp.rest.ws.controller;

import static com.exp.rest.ws.util.JsonUtil.json;
import static com.exp.rest.ws.util.JsonUtil.toJson;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.post;

import com.exp.rest.ws.error.ResponseError;
import com.exp.rest.ws.services.IAbstractService;
import com.exp.rest.ws.services.IExperienceSamplingService;

public class ExperienceSamplingController  extends AbstractController {
	@Override
	public void apply(final IAbstractService iAbstractService) {
		final IExperienceSamplingService experienceSamplingService = (IExperienceSamplingService) iAbstractService;
		//Submit Answer
				post("/submit-sample", (req, res) -> experienceSamplingService.submitExperienceSample(
						req.body()
						) , json());

				after((req, res) -> {
					res.header("Access-Control-Allow-Origin", "*");
					res.header("Access-Control-Allow-Headers", "X-Requested-With");
					res.type("application/json");
				});

				exception(IllegalArgumentException.class, (e, req, res) -> {
					res.status(400);
					res.body(toJson(new ResponseError(e)));
				});

	}
}
