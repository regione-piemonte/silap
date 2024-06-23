/*-
 * ========================LICENSE_START=================================
 * colmirbff
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirbff.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.csi.silap.colmirbff.api.dto.VersamentoEsoneriRidotto;
import it.csi.silap.colmirbff.api.dto.common.PageCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RicercaVersamentoEsoneriResponse extends PageCommonResponse<VersamentoEsoneriRidotto>{
	@Setter
	@Accessors(chain = true)
	public static final class Builder {

		Integer currentPage;
		Integer recordOfPage;
		Integer recordPage;
		Integer totalResult;
		Integer totalPage;
		List <VersamentoEsoneriRidotto> list;
		
		public RicercaVersamentoEsoneriResponse build() {
			RicercaVersamentoEsoneriResponse result = new RicercaVersamentoEsoneriResponse();
			result.setList(list);
			result.setTotalPage(totalPage);
			result.setTotalResult(totalResult);
			result.setRecordOfPage(recordOfPage);
			result.setRecordPage(recordPage);
			result.setCurrentPage(currentPage);
			return result;
		}

}
}

