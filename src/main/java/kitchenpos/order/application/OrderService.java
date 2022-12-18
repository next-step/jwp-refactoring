package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(menuRequest -> {
                    Menu menu = menuRepository.findById(menuRequest.getMenuId()).orElseThrow(NoResultException::new);
                    Long quantity = menuRequest.getQuantity();
                    return new OrderLineItem(menu, quantity);
                }).collect(Collectors.toList());
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(NoResultException::new);

        final Order order = orderRepository.save(new Order(orderTable, orderLineItems));

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(NoResultException::new);
        savedOrder.updateStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.of(savedOrder);
    }
}
