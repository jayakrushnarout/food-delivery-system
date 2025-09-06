package com.springcloud.auth_service.response;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


public class CommonUtil 
{
	public static ResponseEntity<?> createBuildRespose(HttpStatusCode httpstatus,String message,Object data)
	{
		GenricResponseHandler handler=new GenricResponseHandler(httpstatus.value(), message, data);
		
		return handler.createResponse();
	}
		
	
}
