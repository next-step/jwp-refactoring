package kitchenpos.domain;

import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품 관련 도메인 테스트")
public class ProductTest {

    @DisplayName("상품 생성 시, 가격이 비어있으면 에러가 발생한다.")
    @Test
    void createProductThrowErrorWhenPriceIsNull() {
        // when & then
        assertThatThrownBy(() -> generateProduct("감자튀김", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.가격은_비어있을_수_없음.getErrorMessage());
    }

    @ParameterizedTest(name = "상품 생성 시, 가격은 음수이면 에러가 발생한다. (가격: {0})")
    @ValueSource(longs = {-1000, -2030})
    void createProductThrowErrorWhenPriceIsSmallerThanZero(long price) {
        // when & then
        assertThatThrownBy(() -> generateProduct("감자튀김", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.가격은_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("상품 생성 시, 이름이 null이면 에러가 발생한다.")
    @Test
    void createProductThrowErrorWhenNameIsNull() {
        // when & then
        assertThatThrownBy(() -> generateProduct(null, BigDecimal.valueOf(3000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("상품 생성 시, 이름이 비어있이면 에러가 발생한다.")
    @Test
    void createProductThrowErrorWhenNameIsEmpty() {
        // when & then
        assertThatThrownBy(() -> generateProduct("", BigDecimal.valueOf(3000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        String name = "감자튀김";
        BigDecimal price = BigDecimal.valueOf(3000);

        // when
        Product product = generateProduct(name, price);

        // then
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(Name.from(name)),
                () -> assertThat(product.getPrice()).isEqualTo(Price.from(price))
        );
    }
}
