package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table_group.application.TableGroupValidator;
import org.springframework.stereotype.Service;

@Service
public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkValidUngroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }

}
