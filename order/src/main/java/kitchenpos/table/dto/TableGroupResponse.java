package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup savedTableGroup) {
        List<TableResponse> orderTables = savedTableGroup.getOrderTables()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());

        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getOrderTables() {
        return orderTables;
    }
}
