package com.betacom.fe.controllers;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.inputs.AttivitaReq;
import com.betacom.fe.dto.outputs.AttivitaDTO;
import com.betacom.fe.response.Resp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

	private final WebClient webclient;
	
	@GetMapping("/listAttivita")
	public ModelAndView listAttivita(Model model) {
		ModelAndView mav = new ModelAndView("admin/listAttivita");
		List<AttivitaDTO> atti = webclient.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("atttivita/list")
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
	
		model.addAttribute("attivita", atti);
		return mav;
	}
	
	@PostMapping("saveAttivita")
	public String saveAttivita(AttivitaReq req,  RedirectAttributes ra) {
		log.debug("saveAttivita:{}" , req);
		
		String operation = (req.getId() == null) ? "create" : "update";
		String url = "atttivita/" + operation;
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PUT;
		
		ResponseEntity<Resp> response = webclient.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
		}

		
		return "redirect:/admin/listAttivita";
		
	}

	@GetMapping("removeAttivita")
	public Object removeAttivita(@RequestParam (required = true) Integer id, RedirectAttributes ra) {
		log.debug("removeAttivita :" + id);

		ResponseEntity<Resp> response = webclient.delete()
				.uri("atttivita/delete/{id}", id)
				.exchangeToMono(resp -> resp.toEntity(Resp.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
		}

		return "redirect:/admin/listAttivita";
		
	}
	
}
