package com.betacom.fe.dto.outputs;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbbonamentoDTO {
	private Integer id;
	private LocalDate dataInscizione;
	private List<AttivitaDTO> attivita;
}
