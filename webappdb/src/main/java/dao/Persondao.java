package dao;

import java.util.List;

import entity.Person;
import jakarta.annotation.Resource;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Persondao {

	@PersistenceContext(unitName = "person-unit")
	private EntityManager em;

	public Person findPerson(Integer id) {
		return em.find(Person.class, id);
	}

	public List<Person> findAll() {
		return em.createNamedQuery("Person.findAll", Person.class).getResultList();
	}

	public boolean deletePerson(Person person) {
		try {
			em.remove(person);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Transactional
	public boolean addPerson(Person person) {
		try {
			em.persist(person);
		} catch (EntityExistsException | IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Transactional
	public boolean remove(Integer id) {
		Person person = this.findPerson(id);
		if (person != null) {
			try {
				em.remove(person);
			} catch (IllegalArgumentException e) {
				return false;
			}
			return true; // return true if person is removed
		} 
		return false; // return false if person is null
	}

	@Transactional
	public boolean editPerson(Person person) {
		Person updatePerson = this.findPerson(person.getId());
		if (updatePerson != null) {
			updatePerson.setAge(person.getAge());
			updatePerson.setName(person.getName());
			return true;
		}
		return false;

	}

}
