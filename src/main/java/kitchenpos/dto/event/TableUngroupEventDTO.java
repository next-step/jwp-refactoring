package kitchenpos.dto.event;

import java.util.List;

public class TableUngroupEventDTO {

    private List<Long> orderTableIds;

    public TableUngroupEventDTO(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
