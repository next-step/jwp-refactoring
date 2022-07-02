package kitchenpos.service.table.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableUpdateNumberOfGuestsRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableValidator {
    private final OrdersRepository ordersRepository;

    public TableValidator(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException();
        }

        if (ordersRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable, OrderTableUpdateNumberOfGuestsRequest request) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (request.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException("손님 수가 적절하지 않습니다.");
        }
    }


}
