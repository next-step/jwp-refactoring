package kitchenpos.table.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderValidatorImpl(OrderTableRepository orderTableRepository, MenuRepository menuRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    public void validateOrderCreate(Long orderTableId, List<Long> meneIds) {
        validateOrderTable(orderTableId);
        validateMenus(meneIds);
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenus(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderComplete(List<Long> orderTableIds) {
        orderRepository.findAllByOrderTableIdIn(orderTableIds)
                .stream().forEach(order -> order.validateBeforeCompleteStatus());
    }

    public void validateOrderComplete(Long orderTableId) {
        orderRepository.findAllByOrderTableId(orderTableId)
                .stream().forEach(order -> order.validateBeforeCompleteStatus());
    }
}
