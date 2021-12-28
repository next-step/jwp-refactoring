package kitchenpos.order.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import kitchenpos.common.*;
import kitchenpos.menu.repository.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.order.repository.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;
import kitchenpos.table.repository.*;

@Service
public class OrderService {
    private static final String ORDER_TABLE = "주문 테이블";
    private static final String ORDER_LINE_ITEM = "주문 항목";
    private static final String MENU = "메뉴";
    private static final String ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT = "주문 완료가 되지 않았습니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public static OrderService of(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        return new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    public OrderResponse saveOrder(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        Order saveOrder = Order.from(orderTable);
        setUpOrderLineItem(saveOrder, orderRequest.getOrderLineItems());
        return OrderResponse.from(orderRepository.save(saveOrder));
    }

    private void setUpOrderLineItem(Order saveOrder, List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotFoundException(ORDER_LINE_ITEM);
        }

        orderLineItems.stream()
            .forEach(orderLineItem -> saveOrder.addOrderLineItem(
                menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(() -> new NotFoundException(MENU)),
                orderLineItem.getQuantity()
            ));
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse cleanTable(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        checkCompletion(savedOrderTable);
        savedOrderTable.cleanTable();

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkCompletion(OrderTable savedOrderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTable(savedOrderTable);
        boolean isCompleted = orders.stream()
            .allMatch(it -> it.getOrderStatus().equals(OrderStatus.COMPLETION));
        if (!isCompleted) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT);
        }
    }
}

