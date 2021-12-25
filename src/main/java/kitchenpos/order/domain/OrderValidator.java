package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ExceptionMessage;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository,
        MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreate(Order order) {
        validateExistOrderTable(order.getOrderTableId());
        validateExistMenus(order.getMenuIds());
    }

    private void validateExistOrderTable(Long orderTableId) {
        orderTableRepository.findById(orderTableId).orElseThrow(() ->
            new NotFoundException(NOT_FOUND_DATA));
    }

    private void validateExistMenus(List<Long> menuIds) {
        for (Long menuId : menuIds) {
            menuRepository.findById(menuId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.NOT_FOUND_DATA));
        }
    }
}
