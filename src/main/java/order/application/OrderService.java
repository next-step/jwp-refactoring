package order.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.util.*;

import common.*;
import menu.repository.*;
import order.domain.*;
import order.dto.*;
import order.repository.*;
import table.domain.*;
import table.repository.*;

@Service
public class OrderService {
    private static final String ORDER_TABLE = "주문 테이블";
    private static final String ORDER_LINE_ITEM = "주문 항목";
    private static final String MENU = "메뉴";
    private static final String ORDER = "주문";

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
}

