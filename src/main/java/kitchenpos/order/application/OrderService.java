package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Transactional
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository) {

        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.menuIds();
        final Menus menus = new Menus(menuRepository.findAllByIdIn(menuIds));

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("등록되지 않은 메뉴가 있습니다.");
        }

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final OrderLineItems orderLineItems = new OrderLineItems(toOrderLineItemList(orderRequest, menus));
        final Order order = new Order(orderTable, orderLineItems);
        final Order saved = orderRepository.save(order);

        return OrderResponse.of(saved);
    }

    private List<OrderLineItem> toOrderLineItemList(final OrderRequest orderRequest, final Menus menus) {
        return orderRequest.getOrderLineItems()
            .stream()
            .map(it -> new OrderLineItem(menus.get(it.getMenuId()), it.getQuantity()))
            .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeStatus(orderStatus);
        final Order saved = orderRepository.save(order);

        return OrderResponse.of(saved);
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
