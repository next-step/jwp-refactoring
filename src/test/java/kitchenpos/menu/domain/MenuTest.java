package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DisplayName("Menu 테스트")
class MenuTest {

    @Test
    @DisplayName("메뉴 금액이 제품금액 합계보다 초과 시 오류 발생")
    void price_exception() {
        // given
        Menu menu = new Menu("A", BigDecimal.valueOf(100_000.0), new MenuGroup("a"));
        menu.addMenuProduct(new MenuProduct(menu, new Product("aa", BigDecimal.valueOf(10_000.0)), 3L));

        // then
        assertThatThrownBy(() -> menu.validateMenuPrice())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 급액이 제품 합계금액보다 클 수 없습니다.");
    }
}
