package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundEntityException;
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
@Transactional
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
  
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();

        menuService.checkExistsMenus(order.getMenuIds());

        OrderTable persistOrderTable = tableService.findOrderTableById(order.getOrderTableId());
        checkOrderTableIsEmpty(persistOrderTable);

        order.addOrderIdToOrderLineItems();

        return OrderResponse.of(orderRepository.save(order));
    }

    private void checkOrderTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException("빈 테이블일 경우 등록할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order persistOrder = findOrderById(orderId);

        persistOrder.checkOrderStatusIsCompletion();

        persistOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(persistOrder);
    }

    private Order findOrderById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("주문이 등록되어 있지 않습니다."));
    }
}
