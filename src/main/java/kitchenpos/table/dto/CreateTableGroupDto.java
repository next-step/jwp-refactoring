package kitchenpos.table.dto;

import java.util.List;

public class CreateTableGroupDto {

    private List<OrderTableDto> orderTables;

    public CreateTableGroupDto() { }

    public CreateTableGroupDto(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
