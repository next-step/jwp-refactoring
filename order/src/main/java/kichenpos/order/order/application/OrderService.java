package kichenpos.order.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kichenpos.common.exception.NotFoundException;
import kichenpos.order.order.domain.Order;
import kichenpos.order.order.domain.OrderCommandService;
import kichenpos.order.order.domain.OrderLineItem;
import kichenpos.order.order.domain.OrderLineItemMenu;
import kichenpos.order.order.domain.OrderQueryService;
import kichenpos.order.order.ui.request.OrderLineItemRequest;
import kichenpos.order.order.ui.request.OrderRequest;
import kichenpos.order.order.ui.request.OrderStatusRequest;
import kichenpos.order.order.ui.response.OrderResponse;
import kichenpos.order.product.domain.Menu;
import kichenpos.order.product.domain.MenuQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderCommandService commandService;
    private final OrderQueryService queryService;
    private final MenuQueryService menuQueryService;

    public OrderService(OrderCommandService commandService,
        OrderQueryService queryService,
        MenuQueryService menuQueryService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.menuQueryService = menuQueryService;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        return OrderResponse.from(commandService.save(newOrder(request)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listFrom(queryService.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(long id, OrderStatusRequest request) {
        return OrderResponse.from(commandService.changeStatus(id, request.status()));
    }

    private Order newOrder(OrderRequest request) {
        return Order.of(request.getOrderTableId(), orderLineItems(request));
    }

    private List<OrderLineItem> orderLineItems(OrderRequest request) {
        Map<Long, Menu> idToMenu = idToMenuMap(request.menuIds());
        return request.getOrderLineItems()
            .stream()
            .map(itemRequest -> orderLineItem(idToMenu, itemRequest))
            .collect(Collectors.toList());
    }

    private Map<Long, Menu> idToMenuMap(List<Long> menuIds) {
        return menuQueryService.findAllById(menuIds)
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
