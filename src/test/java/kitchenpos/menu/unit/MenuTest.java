package kitchenpos.menu.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.Name;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 단위 테스트")
public class MenuTest {
    private MenuGroup 중식 = MenuGroup.of("중식");
    private Product 짜장면 = Product.of("짜장면", BigDecimal.valueOf(6000));
    private Product 군만두 = Product.of("군만두", BigDecimal.valueOf(2000));
    private MenuProduct 짜장면_1개 = MenuProduct.of(짜장면, 1);
    private MenuProduct 군만두_2개 = MenuProduct.of(군만두, 2);

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // when
        Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식, MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개)));
        // then
        assertAll(
                ()->assertThat(짜장세트.getMenuGroup().getName()).isEqualTo(Name.of("중식")),
                ()->assertThat(짜장세트.getMenuProducts().getSize()).isEqualTo(2)
        );
    }
    @DisplayName("메뉴 가격이 메뉴상품 총 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void createMenu_price_exception() {
        // when - then
        assertThatThrownBy(() -> Menu.of("짜장세트", BigDecimal.valueOf(15000), 중식, MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES);
    }

}
