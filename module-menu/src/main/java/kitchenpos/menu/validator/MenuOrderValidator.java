package kitchenpos.menu.validator;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Component;


@Component
public class MenuOrderValidator implements OrderValidator {

    private MenuRepository menuRepository;

    public MenuOrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validateCreate(Order order) {
        if (order.getOrderLineItems().size() != menuRepository.findAllById(new OrderLineItems(order.getOrderLineItems()).getMenuIds()).size()) {
            throw new IllegalArgumentException();
        }
    }
}
