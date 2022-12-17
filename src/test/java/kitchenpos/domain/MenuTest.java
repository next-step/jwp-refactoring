package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MenuTest {


    @Test
    @DisplayName("메뉴 가격은 모든 상품의 합 이하야야한다.")
    void sumResultLimit() {
        Menu 스테이크_파스타_빅세트 = Menu.of("스테이크_파스타_빅세트", Price.from(BigDecimal.valueOf(6_000)), MenuGroup.from("스테이크_파스타"), null);
        Product 스테이크 = Product.of(BigDecimal.valueOf(4_000), "스테이크");
        Product 파스타 = Product.of(BigDecimal.valueOf(3_000), "파스타");

        MenuProducts menuProducts = new MenuProducts(Arrays.asList(
                MenuProduct.of(스테이크_파스타_빅세트, 스테이크, 2L),
                MenuProduct.of(스테이크_파스타_빅세트, 파스타, 2L))
        );

        assertThatThrownBy(() ->
                스테이크_파스타_빅세트.addMenuProducts(menuProducts)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
