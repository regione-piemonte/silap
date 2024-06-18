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
package it.csi.silap.colmirsrv.integration.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import it.csi.silap.colmirsrv.util.CommonUtils;
import it.csi.silap.colmirsrv.util.reflection.ReflectionUtils;

@Dependent
public class DAO extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	/**
	 * Esegue una query nativa scritta per il database utilizzato. Puo' utilizzare i
	 * placeholder nel formato :nomeCampo che poi verranno sostituiti con
	 * l'effettivo valore del campo corrispondente del bean in input.
	 * 
	 * Il risultato puo' essere mappato in un entity di output che deve avere TANTI
	 * CAMPI QUANTI QUELLI RESTITUITI DALLA QUERY CON ALIAS CORRISPONDENTI La classe
	 * dell'entity in output deve avere necessariamente un campo @Id ma non deve
	 * essere legato ad alcuna tabella. Vedi DecodificaEntity
	 * 
	 * Gestisce anche condizioni del tipo {if(:nomeCampo) AND COLONNA = :nomeCampo}
	 * 
	 * che si attivano nella query solo se il valore di nomeCampo e' !=null
	 * 
	 * @param qryNameOrSql
	 * @param resultCustomEntityClass
	 * @param beanParams
	 * @return
	 */
	public <E> List<E> findNativeQuery(String sql, Class<E> resultCustomEntityClass, Object beanParams) {
		return findNativeQueryPrivate(sql, resultCustomEntityClass, null, beanParams);
	}
	
	
	public <E> List<E> findNativeQuerySql(String sql, Map<String, Object> params, Class<E> resultCustomEntityClass) {
		return findNativeQueryPrivateSql(sql, params, resultCustomEntityClass);
	}
	
	
	@SuppressWarnings("unchecked")
	private <E> List<E> findNativeQueryPrivateSql(String sql, Map<String, Object> params, Class<E> resultCustomEntityClass) {
		Query query = getEntityManager().createNativeQuery(sql, resultCustomEntityClass);
		if (params != null) {
			for (Map.Entry<String, Object> e : params.entrySet())
				query.setParameter(e.getKey(), e.getValue());
		}
		return query.getResultList();
	}
	

	/**
	 * Come sopra ma in input una map di placeholder e valori
	 * 
	 * @param <E>
	 * @param qryNameOrSql
	 * @param resultCustomEntityClass
	 * @param params
	 * @return
	 */
	public <E> List<E> findNativeQuery(String qryNameOrSql, Class<E> resultCustomEntityClass,
			Map<String, Object> params) {
		return findNativeQueryPrivate(qryNameOrSql, resultCustomEntityClass, params, null);
	}

	public <E> E findNativeQuerySingleResult(String qryNameOrSql, Class<E> resultCustomEntityClass,
			Map<String, Object> params) {
		List<E> list = findNativeQueryPrivate(qryNameOrSql, resultCustomEntityClass, params, null);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	
	public Long getNextSequence(String sequanceName) {
		String sql = "SELECT " + sequanceName + ".NEXTVAL FROM DUAL";
		Query query = getEntityManager().createNativeQuery(sql);
		return ((BigDecimal) query.getSingleResult()).longValue();
	}
	
	
	public Long getCountNative(String sql, Map<String, Object> params) {
		Query query = getEntityManager().createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet())
				query.setParameter(entry.getKey(), entry.getValue());
		}
		return ((java.math.BigInteger) query.getSingleResult()).longValue();
	}
	
	public Object callSqlFunction(String functionName, Object param) {
		return callSqlFunction(functionName, new Object[] { param });
	}
	
	public Object callSqlFunction(String functionName, Object[] params) {

		String sql = "SELECT " + functionName + "(";
		
		if (params != null) {
			for (int index = 0; index<params.length; index++)
				sql += (":param" + index + ",");
			sql = sql.substring(0,sql.lastIndexOf(","));
		}
		sql += ") FROM DUAL";
		
		
		Query query = getEntityManager().createNativeQuery(sql);
		if (params != null) {
			int index = 0;
			for (Object param : params) {
				query.setParameter("param" + index++, param);
			}
		}
		
		return query.getSingleResult();
	}
	
	

	
	public Object callSqlProcedure(String procedureName, Class<?>[] classes, Object[] params) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName);
		
		if (params != null) {
			int index = 1;
			for (Object param : params) {
				query.registerStoredProcedureParameter(
					    index,
					    classes[index-1],
					    ParameterMode.IN
					);
				query.setParameter(index, param);
				
				index++;
			}
		}
		query.execute();
				 
		return query.getOutputParameterValue(1);
	}

	
	public void callSqlProcedureNoOutput(String procedureName, Class<?>[] classes, Object[] params) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName);
		
		if (params != null) {
			int index = 1;
			for (Object param : params) {
				query.registerStoredProcedureParameter(
					    index,
					    classes[index-1],
					    ParameterMode.IN
					);
				query.setParameter(index, param);
				
				index++;
			}
		}
		query.execute();
	}
	
	/**
	 * Implementazione privata per eseguire una query nativa con input bean o
	 * hashmap e rimappi il risultato in un oggetto di tipo E
	 * 
	 * @param qryNameOrSql
	 * @param resultCustomEntityClass
	 * @param params
	 * @param beanParams
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <E> List<E> findNativeQueryPrivate(String sql, Class<E> resultCustomEntityClass, Map<String, Object> params,
			Object beanParams) {
		validateQueryParams(params, beanParams);
		sql = loadSqlFromFileIfNecessary(sql);
		sql = replaceConditionalSectionsInStatement(sql, (beanParams != null ? beanParams : params));
		Query query = getEntityManager().createNativeQuery(sql, resultCustomEntityClass);
		// Sostituisco eventuali condizioni if valutando il valore del bean
		if (params != null) {
			for (Map.Entry<String, Object> e : params.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		} else if (beanParams != null) {
			setProperties(query, beanParams);
		}
		return query.getResultList();
	}
	
	
	public Object findNativeQuery(String sql, Map<String, Object> params) {
		sql = loadSqlFromFileIfNecessary(sql);
		sql = replaceConditionalSectionsInStatement(sql,  params);
		Query query = getEntityManager().createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> e : params.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		} 
		return query.getSingleResult();
	}

	/**
	 * Lancia exception nel caso una query riceva parametri sia come map che come
	 * pojo
	 * 
	 * @param params
	 * @param beanParams
	 */
	protected void validateQueryParams(Map<String, Object> params, Object beanParams) {
		if (CommonUtils.isAllNotVoid(params, beanParams)) {
			throw new IllegalArgumentException(
					"i Parametri della query devono essere valorizzati cono come Map<String, Object> o solo come oggetto pojo");
		}
	}

	/**
	 * Sostituisce in uno statement sql le sezioni dinamiche gestite come if
	 * valutando se il valore del placeholder e' diverso da null e sostituendo la
	 * parte successiva dell'if
	 * 
	 * es: condizione nella query: {if(:dataNascita) and trunc(tabella.d_nascita) =
	 * :dataNascita} group(0): {if(:dataNascita) and trunc(tabella.d_nascita) =
	 * :dataNascita} group(1): dataNascita group(2): and trunc(tabella.d_nascita) =
	 * :dataNascita
	 * 
	 *
	 */
	@SuppressWarnings("rawtypes")
	private static String replaceConditionalSectionsInStatement(String sql, Object params) {

		if (sql.indexOf("{if(:") >= 0) {
			Pattern pattern = Pattern.compile("\\{if\\(:([a-zA-Z0-9_]*)\\)(([^{]*))\\}");
			boolean ifConditionExists = true;
			while (ifConditionExists) {
				Matcher matcher = pattern.matcher(sql);
				if (matcher.find()) {
					String ifCondition = matcher.group(0);
					String placeholderName = matcher.group(1);
					String conditionToActivate = matcher.group(2);
					Object fieldValue = null;
					if (params instanceof Map) {
						fieldValue = ((Map) params).get(placeholderName);
					} else {
						fieldValue = ReflectionUtils.getFieldValueByName(params, placeholderName);
					}
					String replacement = (fieldValue == null ? "" : conditionToActivate);
					sql = sql.replace(ifCondition, replacement);
				} else
					ifConditionExists = false;
			}
		}
		return sql;
	}

	/**
	 * Imposta i parametri in una query valutando i field si una classe e
	 * impostandone i valori nei rispettivi placeholders
	 * 
	 * @param query
	 * @param beanParams
	 */
	private static void setProperties(Query query, Object beanParams) {
		for (Parameter<?> p : query.getParameters()) {
			query.setParameter(p.getName(), ReflectionUtils.getFieldValueByName(beanParams, p.getName()));
		}
	}

	/**
	 * Valuta il nome in input, se rappresenta uno statement sql lo ritorna. Se
	 * rappresenta invece il nome file properties e nome query da utilizzare (es:
	 * nomefile.nomequery) carica il file e contenuto della query cercando il file
	 * prima nella posizione della classe chiamante, altrimenti dal path base
	 * 
	 * @param qryNameOrSql
	 * @return
	 */
	public String loadSqlFromFileIfNecessary(String qryNameOrSql) {
		// rappresenta gia' uno statement SQL lo ritorno com'e'
		if (qryNameOrSql.trim().indexOf(' ') > 0)
			return qryNameOrSql;

		// Altrimenti arriva qualcosa tipo: nomefile.nomequery
		String fileName = qryNameOrSql.substring(0, qryNameOrSql.indexOf(".")) + ".properties";

		String queryName = qryNameOrSql.substring(qryNameOrSql.indexOf(".") + 1, qryNameOrSql.length());

		// tentativo 1: cerco il file con le query nella cache
		Properties queryFile = null;

		if (queryFile == null) {
			// tentativo 2: cerco il file nello stesso percorso della classe DAO chiamante
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/sql/" + fileName);

			// tentativo 3: cerco dal path completo passato in ingresso
			if (is == null) {
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			}

			if (is == null) {
				throw new UnsupportedOperationException(
						"file not found in : /sql/" + fileName + " or (absolute path) " + fileName);
			}

			try {
				queryFile = new Properties();
				queryFile.load(is);
			} catch (IOException e) {
				throw new UnsupportedOperationException(
						"Exception in caricamento file /sql/" + fileName + " or (absolute path) " + fileName, e);
			}
		}
		return queryFile.getProperty(queryName);
	}

	/**
	 * Metodo di utilita' per fornire una mappa in output con key nome field e
	 * valore il valore del field prelevandoli da un generico oggetto. Considera
	 * solo quelli con valori non nulli
	 * 
	 * @param beanParams
	 * @return
	 */
	public static Map<String, Object> beanToParamsMap(Object beanParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (Field f : ReflectionUtils.getFields(beanParams)) {
			Object fieldValue = ReflectionUtils.getFieldValueByName(beanParams, f.getName());
			if (fieldValue != null) {
				paramMap.put(f.getName(), fieldValue);
			}
		}
		return paramMap;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends PanacheEntityBase> getClassEntityFromTableName(String tableName) {
		Metamodel meta = entityManager.getMetamodel();
		for (EntityType<?> entity : meta.getEntities()) {
			String tableNameFromEntity = String.join("_", entity.getName().split("(?=\\p{Upper})")).toUpperCase();
			if (tableNameFromEntity.equals(tableName))
				return (Class<PanacheEntityBase>)entity.getJavaType();
		}
		return null;
	}
	
	
	
	public String callFunctionPlSql(String functionName, Object... params) {

		final Set<String> result = new HashSet<String>(1);

		StringBuffer chiamata = new StringBuffer("{? = call " + functionName + "(");

		for (int i = 0; i < params.length; i++) {
			chiamata.append("?,");
		}
		if (params.length > 0)
			chiamata.setLength(chiamata.length() - 1);
		String chiamataStr = chiamata.append(")}").toString();

		Session session = getEntityManager().unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				java.sql.CallableStatement stmt = null;
				try {
					stmt = conn.prepareCall(chiamataStr);
					stmt.registerOutParameter(1, java.sql.Types.VARCHAR);

					for (int i = 0; i < params.length; i++) {
						if (params[i] == null || params[i] instanceof String) {
							stmt.setString(i + 2, (String) params[i]);
						} else if (params[i] instanceof Long) {
							stmt.setLong(i + 2, ((Long) params[i]));
						} else if (params[i] instanceof BigDecimal) {
							stmt.setBigDecimal(i + 2, ((BigDecimal) params[i]));
						} else if (params[i] instanceof java.util.Date) {
							stmt.setTimestamp(i + 2, new java.sql.Timestamp(((java.util.Date) params[i]).getTime()));
						} else {
							throw new IllegalArgumentException(
									"Tipo di parametro sconosciuto:" + params[i].getClass().getName());
						}
					}
					stmt.executeUpdate();
					result.add(stmt.getString(1));
				} finally {
					try {
						if (stmt != null)
							stmt.close();
					} catch (Exception err) {
					}
				}
			}
		});

		return (String) result.toArray()[0];

	}
	
	

}
