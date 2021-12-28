package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.IllegalOrderTableIdsException;
import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.tablegroup.exception.CanNotGroupException;
import kitchenpos.tablegroup.exception.CanNotUnGroupException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {
    private static final String IS_NOT_EMPTY_ERROR_MESSAGE = "주문 테이블이 빈상태일 떄만 단체지정을 생성할 수 있습니다.";
    private static final String ILLEGAL_IDS_ERROR_MESSAGE = "올바르지 않는 아이디 목록 입니다.";
    private static final String IS_COOKING_ERROR_MESSAGE = "조리나 식사 상태일 경우가 아닐 경우에만 해산 할 수 있습니다.";

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void createValidate(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = validateExistOrderTableIds(orderTableIds);
        validateEmptyOrderTable(savedOrderTables);
    }

    public void ungroupValidate(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getCookingAndMealStatus())) {
            throw new CanNotUnGroupException(IS_COOKING_ERROR_MESSAGE);
        }
    }

    private void validateEmptyOrderTable(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new CanNotGroupException(IS_NOT_EMPTY_ERROR_MESSAGE);
            }
        }
    }

    private List<OrderTable> validateExistOrderTableIds(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalOrderTableIdsException(ILLEGAL_IDS_ERROR_MESSAGE);
        }
        return savedOrderTables;
    }
}
