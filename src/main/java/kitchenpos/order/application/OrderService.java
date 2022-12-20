package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private final OrderTableDao orderTableDao;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        validate(request);
        Order order = orderRepository.save(request.toOrder());
        order.addOrderLineItems(request.toOrderLineItems());
        return OrderResponse.of(order);
    }

    private void validate(OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.toOrderLineItems();
        validateNotEmptyOrderLineItems(orderLineItems);
        validateExistsAllMenus(orderLineItems);
        validateNotEmptyOrderTable(request);
    }

    private void validateNotEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void validateExistsAllMenus(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = toMenuIds(orderLineItems);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    private List<Long> toMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }

    private void validateNotEmptyOrderTable(OrderRequest request) {
        Optional<OrderTable> orderTable = orderTableDao.findById(request.getOrderTableId());
        if (!orderTable.isPresent()) {
            throw new IllegalArgumentException("빈 테이블 입니다.");
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(orderRepository.save(order));
    }
}
