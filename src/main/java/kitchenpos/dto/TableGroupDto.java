package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }
}
