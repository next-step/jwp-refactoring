package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderMenuRequest;
import kitchenpos.dto.OrderMenuResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.AlreadyCompleteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            TableRepository tableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    public OrderResponse create(OrderRequest request) {
        OrderTable table = tableRepository.findById(request.getOrderTableId())
                .orElseThrow(EntityNotFoundException::new);
        Order order = new Order(table);

        for (OrderMenuRequest orderMenuRequest : request.getOrderMenuRequests()) {
            Menu menu = menuRepository.findById(orderMenuRequest.getMenuId())
                    .orElseThrow(EntityNotFoundException::new);
            order.add(menu, orderMenuRequest.getQuantity());
        }
        return fromEntity(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        if (savedOrder.isComplete()) {
            throw new AlreadyCompleteException("이미 완료된 주문입니다.");
        }

        savedOrder.changeStatus(orderStatus);
        return fromEntity(savedOrder);
    }

    private OrderResponse fromEntity(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getCreatedTime(), fromOrderMenus(order.getOrderMenus()));
    }

    private List<OrderMenuResponse> fromOrderMenus(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .map(this::fromOrderMenu)
                .collect(Collectors.toList());
    }

    private OrderMenuResponse fromOrderMenu(OrderMenu orderMenu) {
        return new OrderMenuResponse(orderMenu.getId(), orderMenu.getMenuId(), orderMenu.getQuantity());
    }
}
