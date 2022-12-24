package kitchenpos.order.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableGroupValidator;
import kitchenpos.table.domain.TableGroup;

import java.util.Arrays;

import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;

public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateUnGroup(TableGroup tableGroup) {
        validateOrderStatus(tableGroup);
    }

    private void validateOrderStatus(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ORDER_STATUS_EXCEPTION_MESSAGE);
        }
    }
}
