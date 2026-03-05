package com.betacom.fe.dto.inputs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbbonamentoReq {
	private Integer id;
	private String dataInscizione;
	private Integer socioID;
}
