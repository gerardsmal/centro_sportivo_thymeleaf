package com.betacom.fe.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.betacom.fe.dto.inputs.AbbonamentoReq;
import com.betacom.fe.dto.inputs.AttivitaReq;
import com.betacom.fe.dto.outputs.AttivitaDTO;
import com.betacom.fe.dto.outputs.SocioDTO;
import com.betacom.fe.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AbbonamentoController {
	
	private final WebClient webclient;
	
	@GetMapping("listAbbonamento")
	public ModelAndView listAbbonamento(@RequestParam(required = true) Integer id){
		ModelAndView mav = new ModelAndView("listAbbonamento");
		
		SocioDTO soc = webclient.get()
				 .uri(uriBuilder -> uriBuilder
					.path("socio/findById")
					.queryParam("id", id)
					.build())
				 .retrieve()
				 .bodyToMono(SocioDTO.class)
				 .block();

		
		
		mav.addObject("socio", soc);
		mav.addObject("titolo", "ELenco abbonamento per " + soc.getNome() + " " + soc.getCognome());
		return mav;
	}
	
	@GetMapping("createAbbonamento")
	public String createAbbonamento (@RequestParam(required = true) Integer id) {
		log.debug("createAbbonamento {}", id);
		
		AbbonamentoReq req = new AbbonamentoReq();
		req.setSocioID(id);
		req.setDataInscizione(LocalDate.now());
		
		ResponseEntity<Resp> response = webclient.post()
				.uri("abbonamento/create")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		log.debug(response.toString());
		return "redirect:/listAbbonamento?id=" + id;
	}
	
	@GetMapping("aggiungiAttivita")
	public ModelAndView aggiungiAttivita(@RequestParam (required = true) Integer socioId, 
						@RequestParam (required = true) Integer abbonamentoId) {
		log.debug("aggiungiAttivita {}/{} ", socioId, abbonamentoId);
		ModelAndView mav = new ModelAndView("aggiungiAttivita");
		
		List<AttivitaDTO> att = webclient.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("atttivita/list")
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
		
		
		mav.addObject("listAttivita", att);
		
		AttivitaReq req = new AttivitaReq();
		req.setAbbonamentID(abbonamentoId);
		req.setSocioID(socioId);
		mav.addObject("param", req);
		
		
		return mav;
	}
	@PostMapping("saveAttivitaAbbonamento")
	public String saveAttivitaAbbonamento(@ModelAttribute("param") AttivitaReq req) {
		log.debug("saveAttivitaAbbonamento {}", req);
		
		
		
		ResponseEntity<Resp> response = webclient.post()
				.uri("atttivita/createAttivitaAbbonamento")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();
		
		log.debug(response.toString());
		return "redirect:/listAbbonamento?id=" + req.getSocioID();
		
	}
	
	@GetMapping("removeAttivitaAbbonamento")
	public String removeAttivitaAbbonamento(@RequestParam(required = true) Integer abbonamentoId,
			              @RequestParam(required = true) Integer attivitaId,
			              @RequestParam(required = true) Integer socioId) {
		log.debug("removeAttivitaAbbonamento {}/{}/{}", abbonamentoId, attivitaId, socioId);
		
		
		Resp resp = webclient.delete()
				 .uri("atttivita/deleteAttivitaAbbonamento/{abbonamentoId}/{attivitaId}", abbonamentoId, attivitaId)
				 .retrieve()
				 .bodyToMono(Resp.class)
				 .block();
		
		log.debug(resp.toString());

		return "redirect:/listAbbonamento?id=" + socioId;

	}
	

	@GetMapping("removeAbbonamento")
	public String removeAbbonamento(@RequestParam(required = true) Integer abbonamentoId, @RequestParam(required = true) Integer socioId ) {
		log.debug("removeAbbonamento {}", abbonamentoId);
		
		
		Resp resp = webclient.delete()
				 .uri("abbonamento/delete/{abbonamentoId}", abbonamentoId)
				 .retrieve()
				 .bodyToMono(Resp.class)
				 .block();
		
		log.debug(resp.toString());

		return "redirect:/listAbbonamento?id=" + socioId;

	}

	
}
