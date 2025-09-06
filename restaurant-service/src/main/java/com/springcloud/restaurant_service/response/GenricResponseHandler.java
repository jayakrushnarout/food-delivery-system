package com.springcloud.restaurant_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenricResponseHandler 
{
	private Integer status_code;
	private String message;
	private Object data;
	
	public ResponseEntity<?> createResponse()
	{
		Map<String,Object> map=new LinkedHashMap();
		map.put("StatusCode",status_code);
		map.put("message",message);
		map.put("Student", data);
		
		return new ResponseEntity<>(map,HttpStatus.valueOf(status_code));
	}
	
}
