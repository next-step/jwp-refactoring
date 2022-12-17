package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup createTableGroup) {
        return new TableGroupResponse(
                createTableGroup.getId(),
                createTableGroup.getCreatedDate(),
                createTableGroup.getOrderLineItems().stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

}
