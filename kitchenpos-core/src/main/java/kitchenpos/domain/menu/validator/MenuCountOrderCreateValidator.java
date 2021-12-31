package kitchenpos.domain.menu.validator;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.exception.IllegalMenuIdsException;
import kitchenpos.domain.order.validator.OrderCreateValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuCountOrderCreateValidator implements OrderCreateValidator {
    private static final String ILLEGAL_IDS_ERROR_MESSAGE = "메뉴 아이디 목록이 잘못 되었습니다.";
    private final MenuRepository menuRepository;

    public MenuCountOrderCreateValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        final List<Long> menuIds = order.getMenuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalMenuIdsException(ILLEGAL_IDS_ERROR_MESSAGE);
        }
    }
}
