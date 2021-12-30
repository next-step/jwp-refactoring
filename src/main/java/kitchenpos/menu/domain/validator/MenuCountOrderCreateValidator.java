package kitchenpos.menu.domain.validator;

import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.validator.OrderCreateValidator;
import kitchenpos.order.exception.IllegalMenuIdsException;
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
