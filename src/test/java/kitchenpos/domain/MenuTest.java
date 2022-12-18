package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuProductTestFixture.짜장면메뉴상품;
import static kitchenpos.fixture.MenuProductTestFixture.짬뽕메뉴상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    @DisplayName("메뉴 생성작업을 성공한다.")
    @Test
    void of() {
        // given
        String expectedName = "짜장면";
        BigDecimal expectedPrice = 짜장면메뉴상품().totalPrice().value().add(짬뽕메뉴상품().totalPrice().value());
        List<MenuProduct> expectedMenuProduct = Arrays.asList(짜장면메뉴상품(), 짬뽕메뉴상품());

        // when
        Menu 짜장면_탕수육_1인_메뉴_세트 = Menu.of(expectedName, expectedPrice,
                1L, MenuProducts.from(expectedMenuProduct));

        // then
        assertAll(
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트).isNotNull(),
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트.getName()).isEqualTo(expectedName)
        );
    }
}
