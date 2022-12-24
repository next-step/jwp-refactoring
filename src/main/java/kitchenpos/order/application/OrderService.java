package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException());
        findAllMenuById(request.findAllMenuIds());
        List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());
        OrderValidator.validateCreateOrder(orderTable, orderLineItems);
        Order order = request.toOrder(orderTable, OrderStatus.COOKING, orderLineItems);

        return OrderResponse.of(orderRepository.save(order));
    }



    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        validateNotDuplicatedMenuIds(orderLineItemRequests);
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> orderLineItemRequest.toOrderLineItem(findMenuById(orderLineItemRequest.getMenuId())))
                .collect(Collectors.toList());
    }

    private void validateNotDuplicatedMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        boolean duplicatedMenuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .distinct()
                .count() != orderLineItemRequests.size();
        if (duplicatedMenuIds) {
            throw new IllegalArgumentException();
        }
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional(readOnly = true)
    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
        return menus;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.toResponselist(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatus request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());
        order.changeOrderStatus(request);

        return OrderResponse.of(orderRepository.save(order));
    }
}
