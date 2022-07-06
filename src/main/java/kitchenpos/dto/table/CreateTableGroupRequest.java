package kitchenpos.dto.table;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.util.CollectionUtils;

public class CreateTableGroupRequest {

    public static final int MINIMUM_GROUPING_TARGET_SIZE = 2;
    public static final String ORDER_TABLE_IS_LESS_THAN_MINIMUM_GROUP_COUNT_ERROR_MESSAGE = String
        .format("단체석 지정 시, %d개 이상의 주문 테이블 정보가 필요합니다.", MINIMUM_GROUPING_TARGET_SIZE);

    private List<Long> orderTables;

    public CreateTableGroupRequest() {

    }

    public CreateTableGroupRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup(final List<OrderTable> gropingTargetTables) {
        return TableGroup.of(orderTables, gropingTargetTables);
    }

    public List<Long> getOrderTables() {
        validateOrderTable();
        return orderTables;
    }

    void validateOrderTable() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUPING_TARGET_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_LESS_THAN_MINIMUM_GROUP_COUNT_ERROR_MESSAGE);
        }
    }
}
