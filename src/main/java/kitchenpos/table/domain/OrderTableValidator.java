package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableValidator {
    private static final String EXCEPTION_MESSAGE_NUMBER_OF_GUESTS_IS_NOT_NEGATIVE = "가격은 음수일 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_IS_EMPTY_TABLE = "빈 테이블입니다. 요청하신 행위를 실행할 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP = "속해있는 단체 테이블이 있으므로 빈 테이블이 될 수 없습니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL = "현재 속해있는 테이블의 주문이 요리중이거나, 식사중입니다.";
    private static final int ZERO = 0;
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCreate(OrderTable orderTable) {
        validateNumberOfGuests(orderTable.getNumberOfGuests());
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        isOrderStatusNotCompletion(orderTable);
        isAlreadyGrouping(orderTable);
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable, int numberOfGuests) {
        isEmpty(orderTable);
        validateNumberOfGuests(numberOfGuests);
    }

    private void isEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_IS_EMPTY_TABLE);
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < ZERO) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NUMBER_OF_GUESTS_IS_NOT_NEGATIVE);
        }
    }

    private void isOrderStatusNotCompletion(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL);
        }
    }

    private void isAlreadyGrouping(OrderTable orderTable) {
        if (null != orderTable.getTableGroupId()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_ALREADY_IS_TABLE_GROUP);
        }
    }
}
