package kitchenpos.table.dto;

import java.util.List;

public class CreateTableGroupDto {

    private List<OrderTableIdDto> orderTables;

    public CreateTableGroupDto() { }

    public CreateTableGroupDto(List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
