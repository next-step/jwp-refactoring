package kitchenpos.tablegroup.dto;

import java.util.List;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

public class TableGroupRequest {
    private static final int MIN_SIZE = 2;
    private List<OrderTableIdRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public void checkValidSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_SIZE) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_TABLE_GROUP_REQUEST);
        }
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
