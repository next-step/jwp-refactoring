package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupTestFixture.중국집1인메뉴세트그룹;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집1인메뉴세트그룹요청;
import static kitchenpos.fixture.MenuProductTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    @Test
    void of() {
        // when
        String expectedName = "짜장면";
        BigDecimal expectedPrice = BigDecimal.valueOf(8000);
        List<MenuProduct> expectedMenuProduct = Arrays.asList(단무지메뉴상품(), 짬뽕메뉴상품());
        Menu 짜장면_탕수육_1인_메뉴_세트 = Menu.of(expectedName, expectedPrice
                , 중국집1인메뉴세트그룹(중국집1인메뉴세트그룹요청()), MenuProducts.from(expectedMenuProduct));

        // then
        assertAll(
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트).isNotNull(),
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트.getName()).isEqualTo(expectedName)
        );
    }
}
