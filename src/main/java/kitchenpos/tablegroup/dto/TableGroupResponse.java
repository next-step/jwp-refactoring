package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses = new ArrayList<>();

    protected TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    public static TableGroupResponse of(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(id, createdDate, orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
