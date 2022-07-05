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
    private static final int MINIMUM_COUNT = 2;

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateTableGroup(final TableGroup tableGroup) {
        checkAssociateCount(tableGroup);
        tableGroup.getOrderTables().getValue().forEach(this::checkPossibleBelongGroup);
    }

    public void possibleUngroupTableGroup(final TableGroup tableGroup) {
        isPossibleUngroupTable(tableGroup);
    }

    private void checkAssociateCount(TableGroup tableGroup) {
        if (tableGroup.getOrderTables().getValue().size() < MINIMUM_COUNT) {
            throw new IllegalArgumentException("단체 지정에 속한 주문 테이블의 수는 최소 2개 이상이어야 합니다.");
        }
    }

    private void checkPossibleBelongGroup(OrderTable orderTable) {
        if (orderTable.isNotEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블만 새로운 그룹에 속할 수 있습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 단체 지정에 속한 주문 테이블이 존재합니다.");
        }
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
