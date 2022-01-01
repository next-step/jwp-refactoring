package kitchenpos.core.validator;

import kitchenpos.core.domain.MenuRepository;
import kitchenpos.core.domain.Order;
import kitchenpos.core.exception.IllegalMenuIdsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuCountOrderCreateValidatorImpl implements MenuCountOrderCreateValidator {
    private static final String ILLEGAL_IDS_ERROR_MESSAGE = "메뉴 아이디 목록이 잘못 되었습니다.";
    private final MenuRepository menuRepository;

    public MenuCountOrderCreateValidatorImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        final List<Long> menuIds = order.getMenuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalMenuIdsException(ILLEGAL_IDS_ERROR_MESSAGE);
        }
    }
}
