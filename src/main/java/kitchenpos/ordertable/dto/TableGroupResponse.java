package kitchenpos.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;
    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
            tableGroup.getId(),
            tableGroup.getOrderTables().mapList(OrderTableResponse::of),
            tableGroup.getCreatedDate());
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
