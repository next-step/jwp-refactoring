package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuProductTestFixture.짜장면메뉴상품;
import static kitchenpos.fixture.MenuProductTestFixture.짬뽕메뉴상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    @Test
    void of() {
        // when
        long expectedId = 1L;
        String expectedName = "짜장면";
        BigDecimal expectedPrice = BigDecimal.valueOf(8000);
        Long expectedMenuGroupId = 1L;
        List<MenuProduct> expectedMenuProduct = Arrays.asList(짜장면메뉴상품(), 짬뽕메뉴상품());
        Menu 짜장면_탕수육_1인_메뉴_세트 = Menu.of(expectedId, expectedName, expectedPrice
                , expectedMenuGroupId, expectedMenuProduct);

        // then
        assertAll(
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트).isNotNull(),
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트.getId()).isEqualTo(expectedId),
                () -> assertThat(짜장면_탕수육_1인_메뉴_세트.getName()).isEqualTo(expectedName)
        );
    }
}
