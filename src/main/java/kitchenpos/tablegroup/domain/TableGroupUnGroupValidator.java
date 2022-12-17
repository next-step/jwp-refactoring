package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupUnGroupValidator {

    private static final List<OrderStatus> COULD_NOT_UNGROUP_STATUS = Arrays.asList(
            OrderStatus.MEAL, OrderStatus.COOKING);

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupUnGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validate(long tableGroupId) {
        checkExistsByOrderTableIdInAndOrderStatusIn(
                orderTableRepository.findAllByTableGroupId(tableGroupId).stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));
    }

    private void checkExistsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIsList) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIsList,
                COULD_NOT_UNGROUP_STATUS)) {
            throw new IllegalArgumentException("주문 상태는 " + COULD_NOT_UNGROUP_STATUS.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }
}
