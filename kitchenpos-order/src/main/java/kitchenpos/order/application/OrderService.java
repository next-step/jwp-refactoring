package kitchenpos.order.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.common.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderMapper orderMapper
            ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);
        final List<OrderLineItem> orderLineItemList = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest: orderRequest.getOrderLineItemRequests()) {
            final Quantity quantity = new Quantity(orderLineItemRequest.getQuantity());
            final OrderLineItem orderLineItem = new OrderLineItem(savedOrder, orderLineItemRequest.getMenuId(),
                    quantity);
            orderLineItemList.add(orderLineItem);
        }
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        orderLineItemRepository.saveAll(orderLineItemList);
        return orderToOrderResponse(savedOrder);
    }

    private OrderResponse orderToOrderResponse (Order order) {
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
        OrderTable orderTable = orderMapper.getOrderTableById(order.getOrderTableId());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Map<Long, Menu> menus = orderMapper.getMenus(menuIds);
        Map<Long, MenuProducts> menuProducts = getMenuProductByMenuIds(menuIds);
        return OrderResponse.of(order, orderLineItems, menus, orderTable, menuProducts);
    }

    private Map<Long, MenuProducts> getMenuProductByMenuIds(List<Long> menuIds) {
        Map<Long, MenuProducts> menuProductsMap = new HashMap<>();
        for (Long id : menuIds) {
            menuProductsMap.put(id, orderMapper.getMenuProductsByMenuId(id));
        }
        return menuProductsMap;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.change(orderStatus);
        return orderToOrderResponse(savedOrder);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
    }

}
