package kitchenpos.domain;

import kitchenpos.common.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void create() {
        // given
        Product 강정치킨 = new Product("강정치킨", Money.valueOf(19000));

        // when
        MenuProduct menuProduct = new MenuProduct(강정치킨, 2);

        // then
        assertThat(menuProduct).isNotNull();
    }

    @DisplayName("메뉴의 상품의 총 가격을 구할 수 있다.")
    @Test
    void price() {
        // given
        int productPrice = 19000;
        int quantity = 2;
        Product 강정치킨 = new Product("강정치킨", Money.valueOf(productPrice));

        // when
        MenuProduct menuProduct = new MenuProduct(강정치킨, quantity);

        // then
        assertThat(menuProduct.price()).isEqualTo(Money.valueOf(productPrice * quantity));
    }

}
