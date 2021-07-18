package kitchenpos.order.domain;

import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroupLinker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupLinkerImplementation implements TableGroupLinker {

    private final OrderRepository orderRepository;

    public TableGroupLinkerImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusByTableIds(List<Long> orderTableIds) {
        orderTableIds.stream()
                .forEach(orderTableId -> {
                    Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
                    checkCompleted(orders);
                });
    }

    private void checkCompleted(Orders orders) {
        if (!orders.isCompletedAllOrders()) {
            throw new IllegalOrderTableException();
        }
    }
}
