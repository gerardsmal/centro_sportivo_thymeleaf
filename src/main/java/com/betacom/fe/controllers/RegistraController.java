package com.betacom.fe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.inputs.UtenteReq;
import com.betacom.fe.response.Resp;
import com.betacom.fe.security.UtenteServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegistraController {
	
	
	private final WebClient clientWeb;
	private final UtenteServices uS;

	@GetMapping("/registra")
	public ModelAndView registra(Model model) {
		ModelAndView mav = new ModelAndView("registrazione");
		log.debug("registra..");
		
		if (!model.containsAttribute("req")) {
			mav.addObject("req", new UtenteReq());
		}
		return mav;
	}
	
	@PostMapping("/saveNuovoUtente")
	public String saveNuovoUtente(@ModelAttribute("req") UtenteReq req,  RedirectAttributes ra) {
		
		req.setRole("USER");
		
		ResponseEntity<Resp> response = clientWeb.post()
				.uri("utente/create")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			 return "redirect:/registra" ;
		}
		uS.updateUtente(req);
		return "redirect:/login";
		
	}
	
}
