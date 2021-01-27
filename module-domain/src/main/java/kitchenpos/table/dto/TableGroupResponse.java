package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;
    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(),
                tableGroup.getOrderTables().getOrderTables()
                        .stream()
                        .map(it -> OrderTableResponse.of(it))
                        .collect(Collectors.toList()),
                tableGroup.getCreatedDate()
        );
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
