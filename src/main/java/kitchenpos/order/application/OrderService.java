package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.exception.OrderExceptionType;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Menu> savedMenus = menuRepository.findAllById(orderRequest.fetchMenuIds());

        if (orderRequest.getOrderLineItems().size() != savedMenus.size()) {
            throw new OrderException(OrderExceptionType.NOT_MATCH_MENU_SIZE);
        }

        final OrderTable orderTable = findByOrderTableId(orderRequest);
        orderTable.validateUsed();

        final Order save = orderRepository.save(orderRequest.toEntity(savedMenus));

        return OrderResponse.of(save);
    }

    private OrderTable findByOrderTableId(final OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(findAll());
    }

    private List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest updateOrderStatusRequest) {
        final Order savedOrder = findById(orderId);
        savedOrder.updateOrderStatus(updateOrderStatusRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private Order findById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.NOT_FOUND_ORDER_NO));
    }
}
