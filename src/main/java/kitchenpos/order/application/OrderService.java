package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private static final String ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT = "주문이 존재하지 않습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT = "메뉴가 존재하지 않습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_EXIST_MENU = "등록되지 않은 메뉴가 있습니다.";

    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(OrderValidator orderValidator, MenuRepository menuRepository, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());
        Order order = request.toEntity(orderLineItems);
        order.validate(orderValidator);
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.toList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long id, OrderStatusRequest request) {
        Order order = findById(id);
        order.changeStatus(request.status());
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT, id)));
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> requests) {
        validateExistMenu(toMenuIds(requests));
        return requests.stream()
                .map(request -> OrderLineItem.of(findOrderMenu(request.getMenuId()), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> toMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateExistMenu(List<Long> menuIds) {
        Long count = menuRepository.countAllByIdIn(menuIds);

        if (count != menuIds.size()) {
            throw new InvalidParameterException(ERROR_MESSAGE_NOT_EXIST_MENU);
        }
    }

    private OrderMenu findOrderMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT, menuId)));
        return OrderMenu.of(menu.id(), menu.name(), menu.price());
    }
}
