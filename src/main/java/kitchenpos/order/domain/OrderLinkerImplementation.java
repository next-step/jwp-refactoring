package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableLinker;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroupLinker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderLinkerImplementation implements OrderTableLinker, TableGroupLinker {

    private final OrderRepository orderRepository;

    public OrderLinkerImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusByOrderTableId(Long orderTableId) {
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        if (!orders.isCompletedAllOrders()) {
            throw new IllegalOrderTableException();
        }
    }

    @Override
    public void validateOrderStatusByTableIds(List<Long> orderTableIds) {
        orderTableIds.stream()
                .forEach(orderTableId -> validateOrderStatusByOrderTableId(orderTableId));
    }
}
