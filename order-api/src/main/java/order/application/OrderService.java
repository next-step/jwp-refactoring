package order.application;

import common.ErrorMessage;
import common.domain.Price;
import menu.domain.Menu;
import menu.repository.MenuRepository;
import order.domain.Order;
import order.domain.OrderLineItem;
import order.dto.OrderLineItemRequest;
import order.dto.OrderRequest;
import order.dto.OrderResponse;
import order.repository.OrderRepository;
import order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuRepository menuRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        orderValidator.validate(request);
        List<OrderLineItemRequest> requestOrderLineItems = request.getOrderLineItemsRequest();
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(requestOrderLineItems);
        Order savedOrder = orderRepository.save(Order.of(request.getOrderTableId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> requestOrderLineItems) {
        return requestOrderLineItems.stream()
                .map(orderLineItem -> {
                    Menu menu = findMenuById(orderLineItem.getMenuId());
                    return OrderLineItem.of(
                            menu.getId(), orderLineItem.getQuantity(), menu.getName(), Price.from(menu.getPrice())
                    );
                })
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_MENU.getMessage(), id)));
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_ORDER.getMessage(), orderId)));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
