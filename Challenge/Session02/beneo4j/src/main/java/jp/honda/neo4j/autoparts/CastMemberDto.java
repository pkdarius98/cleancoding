package jp.honda.neo4j.autoparts;

public record CastMemberDto(String name, String job, String role) {

    CastMemberDto(String name, String job) {
        this(name, job, null);
    }

    public CastMemberDto withRole(String role) {
        return new CastMemberDto(this.name, this.job, role);
    }
}
