package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class MenuProductTest {

    @Test
    void getAmount() {
        MenuProduct menuProduct = new MenuProduct(null, new Product(null, new Price(2)), 10);

        assertThat(menuProduct.getAmount()).isEqualTo(new Price(20));
    }

    @Test
    @DisplayName("이미 메뉴가 등록되어있으면 변경이 불가능하다.")
    void 이미_메뉴가_등록되어있으면_변경이_불가능하다() {
        MenuProduct menuProduct = new MenuProduct(new Menu(), null, 0);

        assertThatIllegalStateException().isThrownBy(() -> menuProduct.changeMenu(new Menu()));
    }
}