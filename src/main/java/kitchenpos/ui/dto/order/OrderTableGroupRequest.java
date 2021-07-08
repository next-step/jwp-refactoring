package kitchenpos.ui.dto.order;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    protected OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroupRequest of(TableGroup tableGroup) {
        return new OrderTableGroupRequest(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables().stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList()));
    }

    public TableGroup toTableGroup() {
        return TableGroup.of(id, createdDate, orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
