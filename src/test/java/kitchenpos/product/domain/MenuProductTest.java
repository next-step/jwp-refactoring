package kitchenpos.product.domain;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuProduct 클래스 테스트")
public class MenuProductTest {

    @DisplayName("MenuProduct의 가격을 조회한다")
    @Test
    void MenuProduct의_가격_계산_테스트() {
        // given
        Product 레드와인 = 상품(5L, "레드와인", 9000);
        MenuProduct 풀코스_레드와인 = new MenuProduct(null, 레드와인, 2);

        // when
        BigDecimal menuProductPrice = 풀코스_레드와인.getMenuProductPrice();

        // then
        assertThat(menuProductPrice.intValue()).isEqualTo(18000);
    }
}
