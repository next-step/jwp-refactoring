package kitchenpos.order.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.validator.TableValidator;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.validator.TableGroupValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class OrderValidator implements TableValidator, TableGroupValidator {
    private final OrderRepository orderRepository;

    public OrderValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void isPossibleChangeEmpty(OrderTable orderTable) {
        boolean isPossibleChangeEmptyOrderStatus = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("단체 지정에 포함되어 있어서 빈 자리 여부를 변경할 수 없습니다.");
        }
        if (isPossibleChangeEmptyOrderStatus) {
            throw new IllegalArgumentException("주문 상태가 요리중 또는 식사중인 상태인 주문 테이블의 빈 자리 여부는 변경할 수 없습니다.");
        }
    }

    @Override
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
