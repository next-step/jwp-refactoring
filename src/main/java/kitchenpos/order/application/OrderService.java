package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.exception.NotExistOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.OrderRequest;
import kitchenpos.order.presentation.dto.OrderResponse;
import kitchenpos.order.application.exception.BadMenuIdException;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderService(OrderRepository orderRepository, MenuService menuService, OrderTableService orderTableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<Menu> menus = menuService.findByIdIn(orderRequest.getMenuIds());
        List<OrderLineItem> orderLineItems = getOrderLineItemsBy(orderRequest.getOrderLineItems(), menus);
        OrderTable orderTable = orderTableService.findById(orderRequest.getOrderTableId());
        Order order = orderTable.ordered(orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> getOrderLineItemsBy(List<OrderLineItemRequest> orderLineItemRequests, List<Menu> menus) {
        validateCount(orderLineItemRequests, menus);
        return menus.stream()
                .map(menu -> createOrderLineItemWith(orderLineItemRequests, menu))
                .collect(Collectors.toList());
    }

    private void validateCount(List<OrderLineItemRequest> orderLineItemRequests, List<Menu> menus) {
        if (orderLineItemRequests.size() != menus.size()) {
            throw new BadMenuIdException();
        }
    }

    private OrderLineItem createOrderLineItemWith(List<OrderLineItemRequest> orderLineItemRequests, Menu menu) {
        return orderLineItemRequests.stream()
                .filter(orderLineItemRequest -> isMenuIdMatch(orderLineItemRequest, menu))
                .map(orderLineItemRequest -> OrderLineItem.of(menu, orderLineItemRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadMenuIdException::new);
    }

    private boolean isMenuIdMatch(OrderLineItemRequest orderLineItemRequest, Menu menu) {
        return Objects.equals(orderLineItemRequest.getMenuId(), menu.getId());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(NotExistOrderException::new);
    }
}
