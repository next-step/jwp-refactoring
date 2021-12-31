package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import kitchenpos.order.exception.BadOrderRequestException;
import kitchenpos.order.exception.NotCreatedMenuException;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.exception.OrderErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        checkEmptyOrderLineItems(orderRequest);

        List<OrderLineItem> orderLineItems = getValidOrderLineItems(orderRequest);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NotFoundOrderTableException(orderRequest.getOrderTableId()));

        return OrderResponse.of(orderRepository.save(Order.create(orderTable, orderLineItems)));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private void checkEmptyOrderLineItems(OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new BadOrderRequestException(OrderErrorCode.NOT_EXISTS_ORDER_LINE_ITEM);
        }
    }

    private List<OrderLineItem> getValidOrderLineItems(OrderRequest orderRequest) {
        List<Menu> menus = menuRepository.findAllByIdIn(orderRequest.createMenuIds());

        if (isNotCreatedMenu(orderRequest, menus)) {
            throw new NotCreatedMenuException();
        }

        return menus.stream()
                .map(menu -> OrderLineItem.of(menu, orderRequest.findQuantityByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    private boolean isNotCreatedMenu(OrderRequest orderRequest, List<Menu> menus) {
        return menus.size() != orderRequest.getOrderLineItems().size();
    }

}
