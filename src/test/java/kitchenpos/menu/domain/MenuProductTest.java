package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 도메인 테스트")
public class MenuProductTest {
    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void 메뉴_상품_생성() {
        // when
        MenuProduct menuProduct = MenuProduct.of(1L, Quantity.of(50));

        // then
        assertThat(menuProduct.getQuantity()).isEqualTo(50);
    }

    @DisplayName("상품의 가격과 수량을 곱한 총 합계 금액을 계산한다.")
    @Test
    void 메뉴_상품_합계_금액_계산() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, Quantity.of(3L));

        // when
        BigDecimal totalPrice = menuProduct.multiplyByQuantity(BigDecimal.valueOf(500));

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(1500));
    }
}
