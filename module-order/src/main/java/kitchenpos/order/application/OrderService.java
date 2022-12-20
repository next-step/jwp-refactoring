package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.event.OrderCreateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        eventPublisher.publishEvent(new OrderCreateEvent(request.getOrderTableId()));

        List<Menu> menus = findAllMenuById(request.findAllMenuIds());
        Order saveOrder = request.toOrder(request.getOrderTableId(), menus);

        return OrderResponse.from(orderRepository.save(saveOrder));
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("주문 항목의 메뉴의 개수가 일치하지 않습니다.");
        }
        return menus;
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 주문 정보가 올바르지 않습니다."));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(orderRepository.save(savedOrder));
    }
}
