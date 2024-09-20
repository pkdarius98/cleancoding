package jp.honda.neo4j.persons;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Person {
    @Id
    private String name;
    private String mail;

    public Person(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return this.name;
    }

    public String getMail() {
        return this.mail;
    }
}