package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.utils.TestFixture;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private List<MenuProduct> menuProducts;
    private BigDecimal priceSum;

    @BeforeEach
    void setUp() {
        menuProduct1 = TestFixture.메뉴상품_후라이드;
        menuProduct2 = TestFixture.메뉴상품_양념;
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        priceSum = TestFixture.메뉴상품_후라이드.getProductPrice().multiply(BigDecimal.valueOf(TestFixture.메뉴상품_후라이드.getQuantity()))
                .add(TestFixture.메뉴상품_양념.getProductPrice().multiply(BigDecimal.valueOf(TestFixture.메뉴상품_양념.getQuantity())));
    }

    @Test
    @DisplayName("메뉴 구성 상품들의 가격 * 수량을 계산한다.")
    void calculateMenuProductsPrice() {
        //given
        MenuProducts actual = new MenuProducts(menuProducts);

        //when
        Price sum = actual.findPriceSum();

        //then
        assertThat(sum.value()).isEqualByComparingTo(priceSum);
    }
}
