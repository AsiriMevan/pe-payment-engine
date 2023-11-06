package lk.dialog.pe.persistence.service;

import java.text.ParseException;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.persistence.domain.ForcefulRealizeCheques;

public interface ForcefulRealizeChequesService {

	public Object saveForcefulRealizeCheques(ForcefulRealizeCheques forcefulRealizeCheques, String traceId) throws DCPEException,ParseException;
}
