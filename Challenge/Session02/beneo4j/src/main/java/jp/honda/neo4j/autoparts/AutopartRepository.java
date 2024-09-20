package jp.honda.neo4j.autoparts;

import jp.honda.neo4j.persons.Person;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutopartRepository extends Repository<Part, String> {

	@Query("MATCH (part:Part) WHERE part.name_en CONTAINS $name RETURN part SKIP $skip LIMIT $limit")
	List<Part> findSearchResults(@Param("name") String name, @Param("skip") int skip, @Param("limit") int limit);

	@Query("MATCH (part:Part) WHERE part.name_en CONTAINS $name RETURN count(*)")
	int getTotalSearchResults(@Param("name") String name);

	@Query("MATCH (part:Part {name_en: $name})-[rld]->(relatedPart:Part) WHERE type(rld)=$relation " +
			"OPTIONAL MATCH (p:Person)-[:PIC]->(relatedPart) " +
			"RETURN relatedPart")
	List<Part> findSearchRelatedPartResults(@Param("name") String name, @Param("relation") String relation);

    @Query("MATCH (pa:Part {name_en: $part}) MERGE (p:Person {name: $name, mail: $mail}) MERGE (p)-[:PIC]->(pa)")
	void insertPersonData(@Param("name") String name, @Param("mail") String mail, @Param("part") String part);

}


