package fr.soat.icoundoul.dbunit.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.soat.icoundoul.dbunit.entity.Customer;

@Service
@Transactional
public class CustomerService {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Customer> find(String name) {
		Query query = entityManager.createNamedQuery("Customer.find");
		query.setParameter("name", "%" + name + "%");
		return query.getResultList();
	}

	public void remove(int personId) {
		Customer customer = entityManager.find(Customer.class, personId);
		entityManager.remove(customer);
	}

}
