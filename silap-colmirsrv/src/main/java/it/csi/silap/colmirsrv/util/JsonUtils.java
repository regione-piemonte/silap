/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.util;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {

	@Inject
	private static ObjectMapper mapper;
	
	
	private static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	public static String toJson(Object o) {
		try {
			//questo stampa su una riga
			//String result = objectMapper.writeValueAsString(s);
			//questo stampa in modo formattato
			String result = getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
			return result;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("Errore nella conversione in json dell'oggetto "+ o);
		}

	}
	
	
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		try {
			//JSON string to Java Object
			return  getMapper().readValue(jsonString, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("Errore nella conversione in oggetto java a partire da tracciato json "+ jsonString);
		}
	}
	
	public static <T> T fromJsonFile(String jsonFilePath, Class<T> clazz) {
		try {

			return getMapper().readValue(new File(jsonFilePath), clazz);

		} catch (IOException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("Errore nella conversione in oggetto da file " + jsonFilePath);

		}
	}
	
	
}
