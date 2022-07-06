package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {


    @DisplayName("메뉴상품의 수량이 1개이상 이어야한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void quantityValidate(int value) {
        //given
        final Product product = new Product("상품", BigDecimal.ONE);
        int quantity = value;

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new MenuProduct(product, quantity));

    }

    @DisplayName("메뉴상품의 상품은 필수 이다.")
    @Test
    void productValidate() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new MenuProduct(null, 2)
        );

    }
}