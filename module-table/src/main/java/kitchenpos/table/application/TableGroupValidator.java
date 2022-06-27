package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.CreateTableGroupException;
import kitchenpos.table.exception.DontUnGroupException;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public static final int MIN_ORDER_TABLE_COUNT = 2;
    public static final String CREATE_TABLE_GROUP_DEFAULT_RULE = "테이블이 없거나 1개 이하인 경우에는 단체를 생성할 수 없습니다.";
    public static final String CREATE_TABLE_GROUP_ORDER_TABLE_STATUS = "단체로 지정할 테이블이 빈 테이블이거나 이미 단체가 지정된 경우 단체를 생성할 수 없습니다.";

    private final OrderService orderService;

    public TableGroupValidator(OrderService orderService) {
        this.orderService = orderService;
    }

    public void validateGrouping(List<OrderTable> orderTables) {
        validateEmptyAndMinOrderTableCount(orderTables);

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
                throw new CreateTableGroupException(CREATE_TABLE_GROUP_ORDER_TABLE_STATUS);
            }
        }
    }

    public void validateUngroup(List<OrderTable> orderTables) {
        this.validateEmptyAndMinOrderTableCount(orderTables);
        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());

        if (orderService.isExistDontUnGroupState(orderTableIds)) {
            throw new DontUnGroupException();
        }
    }

    private void validateEmptyAndMinOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.isEmpty()) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_DEFAULT_RULE);
        }

        if (orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_DEFAULT_RULE);
        }
    }
}
