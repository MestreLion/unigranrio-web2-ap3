package com.rodrigosilva.ap3.view;

import java.awt.Color;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	private List<Integer> anos;

	@Inject
	private CarroService carroService;

//	private static final int NUM_ANOS  = 50;
	private static final int ANO_ATUAL = LocalDate.now().getYear();
	public  static final int ANO_MIN   = 1950;  // ANO_ATUAL - NUM_ANOS;
	public  static final int ANO_MAX   = ANO_ATUAL + 1;

	private static List<String> fabricantes = new ArrayList<String>(List.of(
		"Alfa Romeo",
		"Audi",
		"BMW",
		"Chevrolet",
		"Ferrari",
		"Fiat",
		"Ford",
		"General Motors",
		"Honda",
		"Hyundai",
		"Kia",
		"Mercedes Benz",
		"Nissan",
		"Renault",
		"Toyota",
		"Volkswagen"
	));

	/* Taken from the (unused) cores64() */
	private static List<String> cores = new ArrayList<String>(List.of(
		"#000000", "#000055", "#0000AA", "#0000FF", "#005500", "#005555", "#0055AA", "#0055FF",
		"#00AA00", "#00AA55", "#00AAAA", "#00AAFF", "#00FF00", "#00FF55", "#00FFAA", "#00FFFF",
		"#550000", "#550055", "#5500AA", "#5500FF", "#555500", "#555555", "#5555AA", "#5555FF",
		"#55AA00", "#55AA55", "#55AAAA", "#55AAFF", "#55FF00", "#55FF55", "#55FFAA", "#55FFFF",
		"#AA0000", "#AA0055", "#AA00AA", "#AA00FF", "#AA5500", "#AA5555", "#AA55AA", "#AA55FF",
		"#AAAA00", "#AAAA55", "#AAAAAA", "#AAAAFF", "#AAFF00", "#AAFF55", "#AAFFAA", "#AAFFFF",
		"#FF0000", "#FF0055", "#FF00AA", "#FF00FF", "#FF5500", "#FF5555", "#FF55AA", "#FF55FF",
		"#FFAA00", "#FFAA55", "#FFAAAA", "#FFAAFF", "#FFFF00", "#FFFF55", "#FFFFAA", "#FFFFFF"
	));

	@PostConstruct
	public void init() {
		carros = carroService.getAll();
		carro.setAno(ANO_ATUAL);
		anos = IntStream.rangeClosed(ANO_MIN, ANO_MAX)
				.map(i -> ANO_MAX - i + ANO_MIN)  // Reverse order
				.boxed().collect(Collectors.toList());
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
	public List<Integer> getAnos() {
		return anos;
	}
	public List<String> getFabricantes() {
		return fabricantes;
	}

	/* Static array */
	public List<String> getCores() {
		return cores;
	}
	/* Reflection on java.awt.Color constants */
	public List<String> getCores16() {
		List<String> cores = new ArrayList<String>();
		for (Field f : Color.class.getFields())
			if (f.getType() == Color.class && f.getName() == f.getName().toUpperCase())
				try { cores.add(String.format("#%06X", ((Color)f.get(null)).getRGB() & 0xFFFFFF)); }
				catch (IllegalAccessException e) {}
		return cores;
	}
	/* Simple R, G, B cube using nested loops, with component "granularity" = 255/3 = 85 */
	public List<String> getCores64() {
		List<String> cores = new ArrayList<String>();
		int[] c = {0, 85, 170, 255};
		for (int r: c) for (int g: c) for (int b: c)
			cores.add(String.format("#%02X%02X%02X\n", r, g, b));
		return cores;
	}

}
