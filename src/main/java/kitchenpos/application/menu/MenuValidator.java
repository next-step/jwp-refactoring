package kitchenpos.application.menu;

import java.math.BigDecimal;
import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.exception.CreateMenuException;
import kitchenpos.exception.MenuPriceException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private static final String MENU_GROUP_IS_NOT_NULL = "메뉴생성 시 메뉴그룹이 필수입니다.";

    private final MenuGroupService menuGroupService;

    public MenuValidator(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    public void validate(Menu menu) {
        MenuGroup menuGroup = menuGroupService.findMenuGroup(menu.getMenuGroupId());
        validateCreateMenu(menuGroup);
        validateMenuPrice(menu);
    }

    private static void validateCreateMenu(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new CreateMenuException(MENU_GROUP_IS_NOT_NULL);
        }
    }

    private void validateMenuPrice(Menu menu) {
        BigDecimal sum = menu.findMenuProducts()
                .stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.findPrice().compareTo(sum) > 0) {
            throw new MenuPriceException(menu.findPrice(), sum);
        }
    }
}
