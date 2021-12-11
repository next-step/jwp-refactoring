package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(MenuDao menuDao,
        OrderRepository orderRepository, TableService tableService) {
        this.menuDao = menuDao;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = tableService.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return OrderResponse.from(orderRepository.save(request.toEntity()));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listFrom(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(long orderId, OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(request.status());
        return OrderResponse.from(savedOrder);
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(long orderTableId, List<OrderStatus> asList) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, asList);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
        List<OrderStatus> asList) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, asList);
    }
}
