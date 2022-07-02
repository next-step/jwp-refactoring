package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 단위테스트")
class MenuTest {
    @DisplayName("메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void priceMoreThen0() {
        assertThatThrownBy(() -> new Menu("세트1", -1, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 비교")
    @ParameterizedTest
    @CsvSource(value = {"100,101,false", "100,100,false", "100,99,true"})
    void moreExpensiveThen(long menuPrice, long otherPrice, boolean moreExpensive) {
        Menu menu = new Menu("메뉴", menuPrice, 1L);
        assertThat(menu.moreExpensiveThen(Price.valueOf(otherPrice))).isEqualTo(moreExpensive);
    }
}
