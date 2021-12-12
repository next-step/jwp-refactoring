package kitchenpos.menu.domain;

import static kitchenpos.menu.sample.ProductSample.십원치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> MenuProduct.of(십원치킨(), Quantity.from(1L)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("상품, 수량은 필수")
    @MethodSource
    void instance_nullProductOrQuantity_thrownIllegalArgumentException(
        Product product, Quantity quantity) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProduct.of(product, quantity))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("가격")
    void price() {
        //given
        Product 십원치킨 = 십원치킨();
        Quantity 두개 = Quantity.from(2L);

        //when
        Price price = MenuProduct.of(십원치킨, 두개).price();

        //then
        assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(20)));
    }

    private static Stream<Arguments> instance_nullProductOrQuantity_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Quantity.from(1L)),
            Arguments.of(십원치킨(), null)
        );
    }
}
