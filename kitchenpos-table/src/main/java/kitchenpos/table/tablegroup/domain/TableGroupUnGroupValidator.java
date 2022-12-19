package kitchenpos.table.tablegroup.domain;

import kitchenpos.orderstatus.domain.Status;
import kitchenpos.orderstatus.repository.OrderStatusRepository;
import org.springframework.stereotype.Component;
import kitchenpos.table.ordertable.domain.OrderTable;
import kitchenpos.table.ordertable.domain.OrderTableRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupUnGroupValidator {

    private static final List<Status> COULD_NOT_UNGROUP_STATUS = Arrays.asList(Status.MEAL, Status.COOKING);

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusRepository orderStatusRepository;

    public TableGroupUnGroupValidator(OrderTableRepository orderTableRepository,
            OrderStatusRepository orderStatusRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    public void validate(long tableGroupId) {
        checkExistsByOrderTableIdInAndOrderStatusIn(
                orderTableRepository.findAllByTableGroupId(tableGroupId).stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));
    }

    private void checkExistsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIsList) {
        if (orderStatusRepository.existsByOrderTableIdInAndStatusIn(orderTableIsList,
                COULD_NOT_UNGROUP_STATUS)) {
            throw new IllegalArgumentException("주문 상태는 " + COULD_NOT_UNGROUP_STATUS.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }
}
