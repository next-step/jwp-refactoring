package kitchenpos.order.application.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupUnGroupValidator;
import kitchenpos.tablegroup.exception.CanNotUnGroupException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableGroupUnGroupValidator implements TableGroupUnGroupValidator {
    private static final String IS_COOKING_ERROR_MESSAGE = "조리나 식사 상태일 경우가 아닐 경우에만 해산 할 수 있습니다.";

    private final OrderRepository orderRepository;

    public OrderTableGroupUnGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getCookingAndMealStatus())) {
            throw new CanNotUnGroupException(IS_COOKING_ERROR_MESSAGE);
        }
    }
}
