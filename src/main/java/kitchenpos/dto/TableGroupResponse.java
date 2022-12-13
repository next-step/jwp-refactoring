package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class TableGroupResponse {

    private Long id;

    private LocalDateTime createdDate;

    private List<OrderTableResponse> orderTables = new ArrayList<>();

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
                              List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = createOrderTables(orderTables);
    }

    private List<OrderTableResponse> createOrderTables(List<OrderTable> orderTablesParam) {
        return orderTablesParam.stream()
                .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }
}
