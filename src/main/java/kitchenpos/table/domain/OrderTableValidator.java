package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void validateChangeEmpty(OrderTable orderTable) {
        checkIsEmptyChangeable(orderTable);
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable, int numberOfGuests) {
        checkIsNumberOfGuestsChangeable(orderTable, numberOfGuests);
    }

    private void checkIsEmptyChangeable(OrderTable orderTable) {
        if (orderTable.isGrouping()) {
            throw new BadRequestException("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
        }

        List<Order> orders = orderRepository.findOrderByOrderTableId(orderTable.getId());
        boolean isComplete = orders.stream()
                .allMatch(Order::isComplete);
        if (!isComplete) {
            throw new BadRequestException("현재 테이블은 주문 완료 상태가 아니므로 빈 테이블 설정을 할 수 없습니다.");
        }
    }

    private void checkIsNumberOfGuestsChangeable(OrderTable orderTable, int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 최소 0명 이상 설정 가능합니다.");
        }

        if (orderTable.isEmpty()) {
            throw new BadRequestException("빈 테이블의 손님 수를 설정할 수 없습니다.");
        }
    }
}
