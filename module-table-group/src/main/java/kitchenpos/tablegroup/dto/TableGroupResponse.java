package kitchenpos.tablegroup.dto;

import kitchenpos.table.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

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
