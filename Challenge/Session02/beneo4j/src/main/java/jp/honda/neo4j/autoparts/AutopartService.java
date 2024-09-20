package jp.honda.neo4j.autoparts;

import jp.honda.neo4j.persons.Person;
import jp.honda.neo4j.persons.PersonRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutopartService {

    private final AutopartRepository autopartRepository;
    private final PersonRepository personRepository;

    private final Neo4jClient neo4jClient;

    private final Driver driver;

    private final DatabaseSelectionProvider databaseSelectionProvider;

    AutopartService(AutopartRepository autopartRepository, PersonRepository personRepository,
                    Neo4jClient neo4jClient,
                    Driver driver,
                    DatabaseSelectionProvider databaseSelectionProvider) {

        this.autopartRepository = autopartRepository;
        this.personRepository = personRepository;
        this.neo4jClient = neo4jClient;
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    public AutopartResponseDto searchPartsByName(String name, int page, int pageSize) {
        List<Part> autopartsList = this.autopartRepository.findSearchResults(name, (page - 1) * pageSize, pageSize);

        int totalRecords = this.autopartRepository.getTotalSearchResults(name);

        AutopartResponseDto response = new AutopartResponseDto(autopartsList, totalRecords, page, pageSize);
        return response;
    }

    public AutopartResponseDto searchRelatedPartsByName(String name, String relation) {
        List<Part> autopartsList = this.autopartRepository.findSearchRelatedPartResults(name, relation);

        int totalRecords = autopartsList.size();

        List<Part> relatedPartList = new ArrayList<>();

        autopartsList.forEach(part -> {
            List<Person> personList = this.personRepository.findPersonRelatedToPart(part.getName_en(), "PIC");

            personList.stream().distinct().forEach(person -> {
                Part partWithPerson = new Part(part.getName_en(), person);
                relatedPartList.add(partWithPerson);
            });
        });

        AutopartResponseDto response = new AutopartResponseDto(relatedPartList, totalRecords, 1, Integer.MAX_VALUE);
        return response;
    }

    /**
     * This is an example of when you might want to use the pure driver in case you have no need for mapping at all, neither in the
     * form of the way the {@link Neo4jClient} allows and not in form of entities.
     *
     * @return A representation D3.js can handle
     */
    public Map<String, List<Object>> fetchPartGraph() {

        var nodes = new ArrayList<>();
        var links = new ArrayList<>();

        try (Session session = sessionFor(database())) {
            var records = session.executeRead(tx -> tx.run("""
                    MATCH (p:PART) <- [r:MENTION] - (p:CHECK_ITEM)
                    WITH m, p ORDER BY m.title, p.name
                    RETURN m.title AS part, collect(p.name) AS check_items
                    """
            ).list());
            records.forEach(record -> {
                var movie = Map.of("label", "part", "title", record.get("movie").asString());

                var targetIndex = nodes.size();
                nodes.add(movie);

                record.get("actors").asList(Value::asString).forEach(name -> {
                    var actor = Map.of("label", "actor", "title", name);

                    int sourceIndex;
                    if (nodes.contains(actor)) {
                        sourceIndex = nodes.indexOf(actor);
                    } else {
                        nodes.add(actor);
                        sourceIndex = nodes.size() - 1;
                    }
                    links.add(Map.of("source", sourceIndex, "target", targetIndex));
                });
            });
        }
        return Map.of("nodes", nodes, "links", links);
    }

    private Session sessionFor(String database) {
        if (database == null) {
            return driver.session();
        }
        return driver.session(SessionConfig.forDatabase(database));
    }

    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

    private AutopartDetailsDto toMovieDetails(TypeSystem ignored, Record record) {
        var movie = record.get("movie");
        return new AutopartDetailsDto(
                movie.get("title").asString(),
                movie.get("cast").asList((member) -> {
                    var result = new CastMemberDto(
                            member.get("name").asString(),
                            member.get("job").asString()
                    );
                    var role = member.get("role");
                    if (role.isNull()) {
                        return result;
                    }
                    return result.withRole(role.asString());
                })
        );
    }
}
