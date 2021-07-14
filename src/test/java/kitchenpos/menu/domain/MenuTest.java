package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 entity 테스트")
class MenuTest {

    private Menu 메뉴;
    @BeforeEach
    void setUp() {
        메뉴 = new Menu(1L, "짜장면 탕수육 메뉴", new BigDecimal(19000), 1L);
    }

    @Test
    void 파라미터로_받은_금액보다_메뉴의_금액이_큰_경우_에러_발생() {
        assertThatThrownBy(() -> 메뉴.validMenuTotalAmount(new BigDecimal(18000))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_entity_에_메뉴_상품_추가() {
        MenuProduct menuProduct = new MenuProduct(1L, 1);
        메뉴.addMenuProducts(menuProduct);
        assertThat(메뉴.getMenuProducts().products().size()).isEqualTo(1);
    }
}
