package kitchenpos.tableGroup.validator;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void possibleUngroupTableGroup(final TableGroup tableGroup) {
        isPossibleUngroupTable(tableGroup);
    }

    private void isPossibleUngroupTable(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().orderTableIds();
        boolean isImpossibleUngroupTable = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        if (isImpossibleUngroupTable) {
            throw new IllegalArgumentException("그룹 해제를 하려는 테이블 중 요리중 또는 식사중인 테이블이 존재합니다.");
        }
    }
}
