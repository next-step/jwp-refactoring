package kitchenpos.menu.application;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;

@Component
public class MenuValidator {
    public void validateMenuPrice(Menu menu, BigDecimal totalPrice) {
        if (menu.isMenuPriceGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
        }
    }
}
