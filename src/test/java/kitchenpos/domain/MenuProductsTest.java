package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴상품은 하나 이상 존재 해야한다.")
    void validate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MenuProducts.from(new ArrayList<>()));
    }

    @Test
    @DisplayName("메뉴상품들의 총금액")
    void totalAmount() {
        //givne
        Product product1 = new Product("상품1", Price.of(20));
        Product product2 = new Product("상품2", Price.of(30));
        MenuProduct menuProduct1 = new MenuProduct(product1, 1);
        MenuProduct menuProduct2 = new MenuProduct(product2, 2);
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2));

        //when
        final Amount totalAmount = menuProducts.totalAmount();

        //then
        assertThat(totalAmount).isEqualTo(Amount.of(80));
    }


}