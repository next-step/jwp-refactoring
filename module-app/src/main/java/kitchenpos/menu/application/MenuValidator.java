package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.common.exception.CannotCreateMenuException;
import kitchenpos.menugroup.domain.MenuGroup;

@Component
public class MenuValidator {

    public void validateMenuPrice(Menu menu, BigDecimal totalPrice) {
        if (menu.isMenuPriceGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
        }
    }

    public void validateExistsMenuGroup(Optional<MenuGroup> menuGroupOptional) {
        menuGroupOptional.orElseThrow(() -> new CannotCreateMenuException("메뉴 그룹이 확인되지 않아 메뉴 등록이 불가합니다."));
    }
}
