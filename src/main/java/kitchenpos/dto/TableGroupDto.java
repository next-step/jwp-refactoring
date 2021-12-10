package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    protected TableGroupDto() {
    }

    private TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        return new TableGroupDto(id, createdDate, orderTables);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return this.orderTables;
    }

}
