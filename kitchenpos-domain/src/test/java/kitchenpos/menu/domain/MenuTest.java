package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {

    private static final String NAME = "name";
    private static final BigDecimal PRICE = BigDecimal.valueOf(10_000);

    @DisplayName("메뉴를 생성한다.")
    @Test
    void constructor() {
        // given
        MenuGroup menuGroup = new MenuGroup("group");

        // when
        Menu menu = new Menu(NAME, PRICE.add(PRICE), menuGroup);

        // then
        assertThat(menu).isNotNull();
        assertThat(menu.getName()).isEqualTo(NAME);
    }

    @DisplayName("메뉴 생성 예외")
    @Test
    void validatePrice() {
        // given
        MenuGroup menuGroup = new MenuGroup("group");

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Menu(NAME, null, menuGroup))
            .withMessage("메뉴 금액은 0보다 커야 한다.");
    }
}
