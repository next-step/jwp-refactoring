package kitchenposNew.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuRepository;
import kitchenposNew.menu.exception.NotFoundMenu;
import kitchenposNew.order.OrderStatus;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderLineItemRepository;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.order.dto.OrderRequest;
import kitchenposNew.order.dto.OrderResponse;
import kitchenposNew.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenposNew.order.exception.NotFoundOrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository
            , OrderLineItemRepository orderLineItemRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();
        if (!orderRequest.isEqualsMenuSize(menuRepository.countByIdIn(menuIds))) {
            throw new NotEqualsOrderCountAndMenuCount();
        }
        List<Menu> menus = menuIds.stream()
                .map(menuId -> menuRepository.findById(menuId).orElseThrow(() -> new NotFoundMenu()))
                .collect(Collectors.toList());

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NotFoundOrderTable());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order persistOrder = orderRepository.save(orderRequest.toOrder(orderTable, menus));
        return OrderResponse.of(persistOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.isOrderStatus(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        savedOrder.changeOrderStatus(order.getOrderStatus());

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId).orElseThrow(IllegalAccessError::new));

        return savedOrder;
    }
}
