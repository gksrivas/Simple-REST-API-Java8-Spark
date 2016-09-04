package com.exp.rest.ws.controller;

import com.exp.rest.ws.services.IAbstractService;

//Marker Interface
public abstract class AbstractController {

	protected abstract void apply(IAbstractService iAbstractService);

}