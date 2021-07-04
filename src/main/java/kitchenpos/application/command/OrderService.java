package kitchenpos.application.command;

import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Long create(OrderCreate orderCreate) {
        Menus menus = new Menus(menuRepository.findAllById(orderCreate.getMenuIdsInOrderLineItems()));
        OrderTable orderTable = orderTableRepository.findById(orderCreate.getOrderTableId())
                .orElseThrow(EntityNotExistsException::new);

        return orderRepository.save(Order.create(orderCreate, menus, orderTable))
                .getId();
    }

    public void changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderStatus);
    }
}
