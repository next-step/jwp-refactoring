package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.UpdateOrderStatusRequest;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderExceptionType;
import kitchenpos.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validate(orderRequest);
        final List<Menu> savedMenus = findAllByMenuId(orderRequest);
        final Order save = orderRepository.save(orderRequest.toEntity(savedMenus));

        return OrderResponse.of(save);
    }

    private List<Menu> findAllByMenuId(final OrderRequest orderRequest) {
        final List<Menu> savedMenus = menuRepository.findAllById(orderRequest.fetchMenuIds());

        if (orderRequest.getOrderLineItems().size() != savedMenus.size()) {
            throw new OrderException(OrderExceptionType.NOT_MATCH_MENU_SIZE);
        }
        return savedMenus;
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(findAll());
    }

    private List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest updateOrderStatusRequest) {
        final Order savedOrder = findById(orderId);
        savedOrder.updateOrderStatus(updateOrderStatusRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private Order findById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.NOT_FOUND_ORDER_NO));
    }
}
