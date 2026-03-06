package com.betacom.fe.dto.inputs;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbbonamentoReq {
	private Integer id;
	private LocalDate dataInscizione;
	private Integer socioID;
}
