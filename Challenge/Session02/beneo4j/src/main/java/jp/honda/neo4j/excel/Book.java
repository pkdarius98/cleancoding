package jp.honda.neo4j.excel;

public class Book {
    private String name;
    private String mail;
    private String department;
    private String part;

    public void setName(String cellValue) {
        this.name = cellValue;
    }

    public void setMail(String cellValue) {
        this.mail = cellValue;
    }

    public void setDepartment(String cellValue) {
        this.department = cellValue;
    }

    public void setPart(String cellValue) {
        this.part = cellValue;
    }
    public String getName() {
        return this.name;
    }

    public String getMail() {
        return this.mail;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getPart() {
        return this.part;
    }
}