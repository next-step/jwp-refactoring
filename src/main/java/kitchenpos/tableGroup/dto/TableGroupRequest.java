package kitchenpos.tableGroup.dto;

import javax.validation.constraints.Size;
import java.util.List;

import static kitchenpos.tableGroup.domain.TableGroup.MIN_ORDER_TABLE_COUNT;

public class TableGroupRequest {
    @Size(min = MIN_ORDER_TABLE_COUNT, message = "단체 지정하려면 주문 테이블이 " + MIN_ORDER_TABLE_COUNT + "이상이어야 합니다.")
    List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public int getSize() {
        return orderTableIds.size();
    }
}
