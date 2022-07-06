package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.menu.dao.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MenuValidator {

    private final MenuRepository menuRepository;

    public MenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderLineItemsCheck(List<Long> menuIds) {
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (menuIds.size() != menuCount) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }
}
