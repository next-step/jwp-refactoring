package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreateService;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatusChangeService;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateService createService;
    private final OrderStatusChangeService statusChangeService;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository,
        OrderCreateService createService,
        OrderStatusChangeService statusChangeService,
        MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.createService = createService;
        this.statusChangeService = statusChangeService;
        this.menuRepository = menuRepository;
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
        return menuRepository.findAllById(request.menuIds())
            .stream()
            .collect(Collectors.toMap(Menu::id, menu -> menu));
    }

    private OrderLineItem orderLineItem(Map<Long, Menu> idToMenu, OrderLineItemRequest request) {
        idToMenu.computeIfAbsent(request.getMenuId(), id -> {
            throw new NotFoundException(String.format("메뉴 id(%d)를 찾을 수 없습니다.", id));
        });

        Menu menu = idToMenu.get(request.getMenuId());
        return OrderLineItem.of(
            request.quantity(), OrderLineItemMenu.of(menu.id(), menu.name(), menu.price()));
    }

}
