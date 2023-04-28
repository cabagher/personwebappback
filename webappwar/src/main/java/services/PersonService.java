package services;

import java.util.List;
import java.util.Optional;

import dao.Persondao;
import entity.Person;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PersonService {

	@Inject
	Persondao persondao;

	public Optional<Person> findPerson(String id) {
		Person person = null;
		try {
			person = persondao.findPerson(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
		if (person == null) {
			return Optional.empty();
		}
		return Optional.of(person);
	}

	public List<Person> findAll() {
		return persondao.findAll();
	}

	public Optional<Person> addPerson(Person person) {
		try {
			Person addPerson = new Person();
			addPerson.setAge(person.getAge());
			addPerson.setName(person.getName());
			persondao.addPerson(person);
		} catch (PersistenceException | IllegalArgumentException e) {
			return Optional.empty();
		}
		return Optional.of(person);
	}

	public boolean deletePerson(Integer id) {
		return persondao.remove(id);
	}

	public boolean editPerson(Person person) {
		return persondao.editPerson(person);
	}

}
