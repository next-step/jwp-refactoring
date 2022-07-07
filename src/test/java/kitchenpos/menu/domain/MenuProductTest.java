package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {
    public static MenuProduct 불고기버거 = MenuProduct.of(ProductTest.불고기버거, 5L);
    public static MenuProduct 새우버거 = MenuProduct.of(ProductTest.새우버거, 1L);
    public static List<MenuProduct> 햄버거_상품 = Arrays.asList(불고기버거, 새우버거);

    @Test
    @DisplayName("메뉴 상품 추가")
    void create() {
        // then
        assertThat(불고기버거).isInstanceOf(MenuProduct.class);
    }

    @Test
    @DisplayName("메뉴 상품 가격 계산")
    void calculate() {
        // when
        BigDecimal calculate = 불고기버거.calculate();
        // then
        assertThat(calculate).isEqualTo(BigDecimal.valueOf(7_500));
    }

}
