package kichenpos.order.order.application;

import java.awt.Menu;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kichenpos.common.exception.NotFoundException;
import kichenpos.order.order.domain.Order;
import kichenpos.order.order.domain.OrderCreateService;
import kichenpos.order.order.domain.OrderLineItem;
import kichenpos.order.order.domain.OrderRepository;
import kichenpos.order.order.domain.OrderStatusChangeService;
import kichenpos.order.order.ui.request.OrderLineItemRequest;
import kichenpos.order.order.ui.request.OrderRequest;
import kichenpos.order.order.ui.request.OrderStatusRequest;
import kichenpos.order.order.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateService createService;
    private final OrderStatusChangeService statusChangeService;

    public OrderService(OrderRepository orderRepository,
        OrderCreateService createService,
        OrderStatusChangeService statusChangeService) {
        this.orderRepository = orderRepository;
        this.createService = createService;
        this.statusChangeService = statusChangeService;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        return OrderResponse.from(createService.create(
            request.getOrderTableId(), orderLineItems(request)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listFrom(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(long id, OrderStatusRequest request) {
        statusChangeService.change(id, request.status());
        return OrderResponse.from(order(id));
    }

    private Order order(long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("주문 id(%d)를 찾을 수 없습니다.", id)));
    }

    private List<OrderLineItem> orderLineItems(OrderRequest request) {
        Map<Long, Menu> idToMenu = idToMenuMap(request);
        return request.getOrderLineItems()
            .stream()
            .map(itemRequest -> orderLineItem(idToMenu, itemRequest))
            .collect(Collectors.toList());
    }

    private Map<Long, Menu> idToMenuMap(OrderRequest request) {
        return null;
//        menuRepository.findAllById(request.menuIds())
//            .stream()
//            .collect(Collectors.toMap(Menu::id, kitchenpos.product.menu -> kitchenpos.product.menu));
    }

    private OrderLineItem orderLineItem(Map<Long, Menu> idToMenu, OrderLineItemRequest request) {
        idToMenu.computeIfAbsent(request.getMenuId(), id -> {
            throw new NotFoundException(String.format("메뉴 id(%d)를 찾을 수 없습니다.", id));
        });

        Menu menu = idToMenu.get(request.getMenuId());
        return OrderLineItem.of(
            request.quantity(),
            null); //OrderLineItemMenu.of(kitchenpos.product.menu.id(), kitchenpos.product.menu.name(), kitchenpos.product.menu.price()));
    }
}
