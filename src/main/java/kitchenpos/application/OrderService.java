package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderMenuRequest;
import kitchenpos.dto.OrderMenuResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        addMenus(order, request.getOrderMenuRequests());

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

        savedOrder.changeStatus(orderStatus);
        return fromEntity(savedOrder);
    }

    private void addMenus(Order order, List<OrderMenuRequest> orderMenuRequests) {
        Map<Long,OrderMenuRequest> orderMenuRequestMap = orderMenuRequests.stream()
                .collect(Collectors.toMap(OrderMenuRequest::getMenuId, it -> it));
        List<Menu> menus = findMenus(orderMenuRequestMap.keySet());

        for (Menu menu : menus) {
            OrderMenuRequest request = orderMenuRequestMap.get(menu.getId());
            order.add(menu, request.getQuantity());
        }
    }

    private List<Menu> findMenus(Set<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menus.size() != menuIds.size()) {
            throw new EntityNotFoundException("등록되지 않은 메뉴입니다.");
        }
        return menus;
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
