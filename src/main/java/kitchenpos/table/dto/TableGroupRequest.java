package kitchenpos.table.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupRequest {
    private static final String EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS = "유효한 Table ID 가 없습니다! 값을 확인해주세요.";

    private List<Long> orderTableIds;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        validateOrderTableIds(orderTableIds);
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS);
        }
    }
}
