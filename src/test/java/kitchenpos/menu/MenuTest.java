package kitchenpos.menu;

import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("메뉴 상품목록이 비어있는 경우 오류 테스트")
    @Test
    void createMenuEmptyMenuProductsExceptionTest() {
        //given
        final MenuGroup 분식 = new MenuGroup("분식");
        final Price price = new Price(new BigDecimal(14000));
        final MenuProducts emptyMenuProducts = new MenuProducts(Arrays.asList());

        //when
        //then
        assertThatThrownBy(() -> new Menu(1L, "새메뉴", price, 분식, emptyMenuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 총합이 메뉴의 가격보다 적거나 같은 경우 오류 발생")
    @Test
    void productPriceSumUpperThanMenuPriceExceptionTest() {
        //given
        final MenuGroup 분식 = new MenuGroup("분식");
        final Price menuPrice = new Price(new BigDecimal(14000));
        final Product 돈까스 = new Product("돈까스", new Price(new BigDecimal(7000)));
        final Product 비빔냉면 = new Product("비빔냉면", new Price(new BigDecimal(6000)));
        final MenuProduct 메뉴구성1 = new MenuProduct(돈까스, new Quantity(1));
        final MenuProduct 메뉴구성2 = new MenuProduct(비빔냉면, new Quantity(1));
        final MenuProducts menuProducts = new MenuProducts(Arrays.asList(메뉴구성1, 메뉴구성2));

        //when
        //then
        assertThatThrownBy(() -> new Menu(1L, "라볶이세트", menuPrice, 분식, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
