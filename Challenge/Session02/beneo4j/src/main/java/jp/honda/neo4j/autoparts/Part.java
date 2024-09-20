package jp.honda.neo4j.autoparts;

import jp.honda.neo4j.persons.Person;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Node
public class Part {
	@Id
	private final String name_en;
	private final Person person;

	public Part(String name_en, Person person) {
		this.name_en = name_en;
		this.person = person;
	}
	public String getName_en() {
		return this.name_en;
	}
	public Person getPerson() {
		return this.person;
	}

}
