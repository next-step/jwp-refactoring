package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            TableService tableService
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        validate(request);
        Order order = orderRepository.save(request.toOrder());
        order.addOrderLineItems(toOrderLineItems(request));
        return OrderResponse.of(order);
    }

    private void validate(OrderRequest request) {
        List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();
        List<Long> menuIds = request.toMenuIds();
        validateNotEmptyOrderLineItems(orderLineItems);
        validateExistsAllMenus(menuIds);
        validateNotEmptyOrderTable(request.getOrderTableId());
    }

    private void validateNotEmptyOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException();
        }
    }

    private void validateExistsAllMenus(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new InvalidOrderException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    private void validateNotEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = tableService.findById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("빈 테이블 입니다.");
        }
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest request) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            Menu menu = findMenuById(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toOrderLineItem(menu));
        }
        return orderLineItems;
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(NotFoundMenuException::new);
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(NotFoundOrderException::new);
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(orderRepository.save(order));
    }
}
