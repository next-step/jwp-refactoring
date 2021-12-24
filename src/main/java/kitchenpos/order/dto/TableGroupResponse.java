package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;
    
    private TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }
    
    public static TableGroupResponse of(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroupResponse(id, createdDate, orderTables);
    }
    
    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, orderTables);
    }
}
