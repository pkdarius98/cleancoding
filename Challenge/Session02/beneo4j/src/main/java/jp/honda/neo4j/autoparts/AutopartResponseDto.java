package jp.honda.neo4j.autoparts;

import java.util.List;

public record AutopartResponseDto(List<Part> parts, int totalRecords, int page, int pageSize) {
}
