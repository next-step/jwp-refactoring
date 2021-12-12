package kitchenpos.menu.domain;

import static kitchenpos.menu.sample.ProductSample.십원치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품들")
class MenuProductsTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> MenuProducts.singleton(
                MenuProduct.of(십원치킨(), Quantity.from(1L))
            ));
    }

    @Test
    @DisplayName("메뉴 상품 리스트 필수")
    void instance_nullMenuProducts_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProducts.from(null))
            .withMessage("메뉴 상품 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("메뉴 상품 리스트에 null이 포함될 수 없음")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProducts.from(Collections.singletonList(null)))
            .withMessageEndingWith("null이 포함될 수 없습니다.");
    }

    @Test
    @DisplayName("합산 금액")
    void sumPrice() {
        //given
        MenuProduct menuProduct1 = MenuProduct.of(십원치킨(), Quantity.from(2L));
        MenuProduct menuProduct2 = MenuProduct.of(십원치킨(), Quantity.from(3L));

        //when
        Price sumPrice = MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2))
            .sumPrice();

        //then
        assertThat(sumPrice).isEqualTo(Price.from(BigDecimal.valueOf(50)));
    }

}
