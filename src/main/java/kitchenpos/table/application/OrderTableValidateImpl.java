package kitchenpos.table.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;

import kitchenpos.exception.KitchenposException;
import kitchenpos.order.application.OrderTableValidator;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidateImpl implements OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidateImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void checkExistsOrderTable(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new KitchenposException(NOT_FOUND_ORDER_TABLE);
        }
    }
}
