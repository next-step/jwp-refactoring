package kitchenpos.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup, List<OrderTable> orderTables) {
        this(tableGroup.getId(), tableGroup.getCreatedDate(),
            orderTables.stream().map(OrderTableResponse::of)
                .collect(Collectors.toList()));
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
