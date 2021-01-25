package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuService menuService,
            final TableService tableService,
            final OrderRepository orderRepository
    ) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();

        menuService.checkExistsMenus(order.getMenuIds());
        OrderTable persistOrderTable = tableService.findOrderTableById(order.getOrderTableId());

        if (persistOrderTable.isEmpty()) {
            throw new EmptyOrderTableException("빈 테이블일 경우 등록할 수 없습니다.");
        }

        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }
//
//    @Transactional
//    public Order changeOrderStatus(final Long orderId, final Order order) {
//        final Order savedOrder = orderDao.findById(orderId)
//                .orElseThrow(IllegalArgumentException::new);
//
//        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
//            throw new IllegalArgumentException();
//        }
//
//        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
//        savedOrder.setOrderStatus(orderStatus.name());
//
//        orderDao.save(savedOrder);
//
//        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
//
//        return savedOrder;
//    }
}
