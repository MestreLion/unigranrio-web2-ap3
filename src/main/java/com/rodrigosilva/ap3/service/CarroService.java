package com.rodrigosilva.ap3.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import com.rodrigosilva.ap3.model.Carro;

@Stateless
public class CarroService {

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Carro carro) {
		entityManager.persist(carro);
	}

	public void update(Carro carro) {
		entityManager.merge(carro);
	}

	public void delete(Carro carro) {
		entityManager.merge(carro);
	}

	public List<Carro> getAll() {
		CriteriaQuery<Carro> cq = entityManager.getCriteriaBuilder().createQuery(Carro.class);
		cq.select(cq.from(Carro.class));
		return entityManager.createQuery(cq).getResultList();
	}
}
