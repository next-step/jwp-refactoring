package kitchenpos.menu.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.application.OrderMenuValidator;

@Component
public class MenuValidator implements OrderMenuValidator {
    @Autowired
    private MenuRepository menuRepository;

    public void validateMenuPrice(Menu menu, BigDecimal totalPrice) {
        if (menu.isMenuPriceGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
        }
    }

    @Override
    public void validateExistsMenuById(Long menuId) {
        menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("조회된 메뉴가 없습니다. 입력 ID : " + menuId));
    }
}
