package kitchenpos.menu.domain;

import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("메뉴 생성시 가격은 0보다 커야 한다.")
    @Test
    void validationPrice() {
        MenuGroup 치킨류 = MenuGroupFixture.생성("치킨");

        assertThatThrownBy(
                () -> MenuFixture.생성("후라이드두마리세트", new BigDecimal("-10"), 치킨류)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 메뉴 상품 추가")
    @Test
    void addMenuProducts() {
        MenuProduct 메뉴상품 = MenuProductFixture.후라이드두마리();
        Menu 후라이드두마리세트 = MenuFixture.샘플();

        후라이드두마리세트.addMenuProducts(Arrays.asList(메뉴상품));

        assertThat(후라이드두마리세트.getMenuProducts()).isEqualTo(Arrays.asList(메뉴상품));
    }
}
