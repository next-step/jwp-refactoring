package table_group.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequestDto {

    private List<Long> orderTableIds = new ArrayList<>();

    public TableGroupRequestDto() {
    }

    public TableGroupRequestDto(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
