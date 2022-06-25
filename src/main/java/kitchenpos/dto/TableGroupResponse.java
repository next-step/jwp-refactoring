package kitchenpos.dto;

import kitchenpos.domain.TableGroupEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> orderTables;

    protected TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroupEntity tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getOrderTables()
                        .stream()
                        .map(table -> TableResponse.of(table))
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }
}
