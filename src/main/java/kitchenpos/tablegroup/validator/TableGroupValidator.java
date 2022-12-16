package kitchenpos.tablegroup.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupValidator(OrderTableRepository orderTableRepository,
                               OrderRepository orderRepository,
                               OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public void validateCreation(Long tableGroupId, List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds)
                .orElseThrow(() -> new IllegalArgumentException("등록 된 주문 테이블에 대해서만 단체 지정이 가능합니다"));

        orderTables.forEach(orderTable -> {
            validateOrderTableEmpty(orderTable);
            validateAlreadyTableGroup(orderTable);
        });
        validateOrderTablesSize(orderTables);
        addAllOrderTables(tableGroupId, orderTables);
    }

    public void validateUngroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findListByTableGroupId(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정을 해제할 주문 테이블이 없습니다"));

        validateOrderStatus(orderTables);
    }

    private void validateOrderStatus(List<OrderTable> orderTables) {
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        orderTables.forEach(orderTable -> {
            Optional<Order> findSameStatus = orderRepository.findByOrderTableId(orderTable.getId())
                    .stream()
                    .filter(order -> order.isSameStatus(orderStatuses))
                    .findAny();
            if (findSameStatus.isPresent()) {
                throw new IllegalArgumentException("조리, 식사 상태의 주문이 포함 된 주문 테이블은 상태를 변경할 수 없습니다");
            }
        });
    }

    private void validateAlreadyTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블이 포함되어 있습니다[orderTable:" + orderTable + "]");
        }
    }

    private void validateOrderTableEmpty(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException(
                    "비어있지 않은 주문 테이블은 단체 지정을 할 수 없습니다[orderTable" + orderTable + "]");
        }
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("최소 2개 이상의 주문 테이블들에 대해서만 단체 지정이 가능합니다");
        }
    }

    private void addAllOrderTables(Long tableGroupId, List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            orderTable.changeEmpty(false, orderTableValidator);
            orderTable.changeTableGroupId(tableGroupId);
        });
    }
}
