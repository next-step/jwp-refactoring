package kitchenpos.table.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.persistence.TableGroupRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableValidator {
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateTableUnGroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(tableGroup.orderTableIds());
        orders.forEach(Order::validateBeforeCompleteStatus);
    }

    public void validateTableEmpty(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.forEach(Order::validateBeforeCompleteStatus);
    }
}

