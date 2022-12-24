package kitchenpos.menu.validator;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Component;


@Component
public class MenuOrderValidator implements OrderValidator {

    public static final String ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";

    private MenuRepository menuRepository;

    public MenuOrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validateCreate(Order order) {
        if (order.getOrderLineItems().size() != menuRepository.findAllById(new OrderLineItems(order.getOrderLineItems()).getMenuIds()).size()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
        }
    }
}
