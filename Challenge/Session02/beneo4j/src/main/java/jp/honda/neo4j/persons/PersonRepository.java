package jp.honda.neo4j.persons;

import jp.honda.neo4j.autoparts.Part;
import jp.honda.neo4j.persons.Person;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends Repository<Person, String> {
	@Query("MATCH (p:Person)-[rld]->(relatedPart:Part) WHERE type(rld)=$relation RETURN p")
	List<Person> findPersonRelatedToPart(@Param("name") String name, @Param("relation") String relation);
}
