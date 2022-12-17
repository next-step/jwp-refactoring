package kitchenpos.menu.unit;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("메뉴기능")
public class MenuTest {

    @Test
    @DisplayName("메뉴의 금액검증 기능 (메뉴의 최종 금액은 메뉴가 포함하는 상품의 금액의 합보다 크면 안된다)")
    void menuTest1() {
        BigDecimal _3000 = new BigDecimal(3000);
        BigDecimal _2000 = new BigDecimal(2000);

        Menu menu = Menu.create("menu", _3000, null);

        assertThatThrownBy(() -> menu.validatePrice(new Price(_2000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
