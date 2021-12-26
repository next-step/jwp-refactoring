package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        List<Long> menuIds = orderRequest.getMenuIds();
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menus.size() != menus.size()) {
            throw new IllegalArgumentException("일부 메뉴가 존재하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new EntityNotFoundException("주문 테이블이 존재하지 않습니다. orderTableId = " + orderRequest.getOrderTableId()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블에서는 주문할 수 없습니다.");
        }

        final Order order = Order.create(orderTable);

        for(Menu menu : menus) {
            final OrderLineItemRequest orderLineItemRequest = orderRequest.find(menu);
            order.addItem(menu, orderLineItemRequest.getQuantity());
        }

        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문이 존재하지 않습니다. orderId = " + orderId));

        order.nextOrderStatus();
        return OrderResponse.of(order);
    }
}
