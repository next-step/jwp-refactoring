package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {

    private OrderTableRepository orderTableRepository;
    private OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTables group(List<Long> orderTableIds, TableGroup tableGroup) {
        List<OrderTable> orderTableList = orderTableIds.stream()
                .map(id -> orderTableRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage())))
                .collect(Collectors.toList());

        OrderTables orderTables = new OrderTables(orderTableList);
        orderTables.group(tableGroup);
        return orderTables;
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            Order order = orderRepository.findOrderByOrderTable(orderTable)
                    .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
            order.checkCookingOrMeal();
            orderTable.ungroup();
        }
    }
}
