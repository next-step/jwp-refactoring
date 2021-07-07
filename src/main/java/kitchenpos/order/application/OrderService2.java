package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderService2 {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    //TODO: TableRepository 로 리팩토링 후 변경
    private final OrderTableDao orderTableDao;

    public OrderService2(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    public OrderResponse create(final OrderRequest request) {
        validateDuplicatedMenus(request.getMenuIds());
        validateOrderTable(request.getOrderTableId());
        return OrderResponse.from(orderRepository.save(request.toEntity()));
    }

    private void validateOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateDuplicatedMenus(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        OrderEntity orderEntity = findOrderById(orderId);
        orderEntity.changeStatus(request.getOrderStatus());
        return OrderResponse.from(orderEntity);
    }

    private OrderEntity findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
