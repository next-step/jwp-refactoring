package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.DuplicateMenuException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        validateDuplicatedMenu(orderRequest);
        final List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(EntityNotFoundException::new);
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private void validateDuplicatedMenu(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(toList());

        if (orderRequest.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new DuplicateMenuException("주문시 주문항목에 메뉴들은 중복될 수 없습니다.");
        }
    }

    private List<OrderLineItem> toOrderLineItems(final OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(orderLineItem -> {
                    Menu menu = menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(EntityNotFoundException::new);
                    return new OrderLineItem(menu, orderLineItem.getQuantity());
                }).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}
