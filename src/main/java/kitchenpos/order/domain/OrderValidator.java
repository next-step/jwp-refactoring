package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private static final String MENU_NOT_EXIST = "존재하지 않는 메뉴가 포함되어 있습니다.";

    private final MenuRepository menuRepository;
    private final PlaceOrderValidator placeOrderValidator;

    public OrderValidator(MenuRepository menuRepository, PlaceOrderValidator placeOrderValidator) {
        this.menuRepository = menuRepository;
        this.placeOrderValidator = placeOrderValidator;
    }

    public void validate(Order order) {
        validateMenu(order);
        placeOrderValidator.validateTableEmpty(order.getOrderTableId());
    }

    private void validateMenu(Order order) {
        List<Long> assignedMenus = order.findMenus();
        if (menuRepository.countByIdIn(assignedMenus) != assignedMenus.size()) {
            throw new IllegalArgumentException(MENU_NOT_EXIST);
        }
    }
}
