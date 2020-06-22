package com.rodrigosilva.ap3.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rodrigosilva.ap3.model.Carro;
import com.rodrigosilva.ap3.service.CarroService;

@Named
@RequestScoped
public class CarroBean {
	private Carro carro = new Carro();
	private List<Carro> carros;

	@Inject
	private CarroService carroService;

	@PostConstruct
	public void init() {
		carros = carroService.getAll();
	}

	public String submit() {
		carroService.create(carro);
		carros.add(carro);
		carro = new Carro();
		return null;
	}

	//////////////////////////////////////////////

	public Carro getCarro() {
		return carro;
	}
	public List<Carro> getCarros() {
		return carros;
	}
}
