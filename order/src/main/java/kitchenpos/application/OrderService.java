package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
            throw new IllegalArgumentException("메뉴가 선택되지 않았습니다.");
        }

        final List<Long> menuIds = extractMenuIds(request);
        List<Menu> menus = menuRepository.findAllByIdIn(menuIds);

        if (menus.size() != request.getOrderLineItemSize()) {
            throw new IllegalArgumentException("요청 메뉴와 실제 메뉴 개수가 일치하지 않습니다.");
        }

        if (orderRepository.existsOrderTableById(request.getOrderTableId()) == null) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다..");
        }

        if (orderRepository.isEmptyTable(request.getOrderTableId())) {
            throw new IllegalArgumentException("주문 테이블은 빈 테이블이 아니어야 합니다.");
        }

        return orderRepository.save(new Order(request.getOrderTableId(), OrderStatus.COOKING, toOrderLineItems(menus, request.getOrderLineItems())));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(NoSuchElementException::new);

        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return order;
    }

    public List<Long> extractMenuIds(OrderRequest request) {
        return request.getOrderLineItems()
                      .stream()
                      .map(OrderLineItemRequest::getMenuId)
                      .collect(Collectors.toList());
    }

    public List<OrderLineItem> toOrderLineItems(List<Menu> menus, List<OrderLineItemRequest> OrderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItem : OrderLineItemRequests) {
            menus.stream()
                 .filter(menu -> menu.getId()
                                     .equals(orderLineItem.getMenuId()))
                 .findFirst()
                 .ifPresent(menu -> orderLineItems.add(new OrderLineItem(menu, orderLineItem.getQuantity())));
        }
        return orderLineItems;
    }
}
