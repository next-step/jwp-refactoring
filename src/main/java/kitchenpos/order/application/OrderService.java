package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.InvalidMenuNumberException;
import kitchenpos.exception.NotExistException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> orderMenuIds = orderRequest.getOrderMenuIds();
        if (orderMenuIds.size() != menuRepository.countByIdIn(orderMenuIds)) {
            throw new InvalidMenuNumberException();
        }

        final OrderTable persistOrderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(NotExistException::new);
        final Orders order = new Orders(persistOrderTable);

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(NotExistException::new);
            order.addOrderMenu(menu, Quantity.of(orderLineItemRequest.getQuantity()));
        }

        final Orders persist = orderRepository.save(order);
        return persist.toOrderResponse();
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(Orders::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Orders persistOrder = orderRepository.findById(orderId)
                .orElseThrow(NotExistException::new);
        persistOrder.changeStatus(orderStatusRequest.getOrderStatus());
        return persistOrder.toOrderResponse();
    }
}
