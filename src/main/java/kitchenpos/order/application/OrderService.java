package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuExceptionCode;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.order.exception.OrderTableExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public OrderResponse create(OrderRequest request) {
        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        List<Menu> menus = findAllMenuById(request.findAllMenuIds());
        Order order = request.toOrder(orderTable, OrderStatus.COOKING, menus);

        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if(menuIds.size() != menus.size()) {
            throw new EntityNotFoundException(MenuExceptionCode.NOT_FOUND_BY_ID.getMessage());
        }

        return menus;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.list(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus request) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(request);

        return OrderResponse.of(orderRepository.save(order));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(OrderExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }
}
