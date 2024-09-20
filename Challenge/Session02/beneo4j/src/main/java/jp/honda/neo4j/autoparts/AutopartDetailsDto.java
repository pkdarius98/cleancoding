package jp.honda.neo4j.autoparts;

import java.util.List;

public record AutopartDetailsDto(String title, List<CastMemberDto> cast) {
}
