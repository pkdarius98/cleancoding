package jp.honda.neo4j.mail;

import java.util.List;

public class MailData {
    private String email;
    private List<String> partName;

    public String getEmail() {
        return email;
    }
    public List<String> getPartName() {
        return partName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPartName(List<String> partName) {
        this.partName = partName;
    }
}
