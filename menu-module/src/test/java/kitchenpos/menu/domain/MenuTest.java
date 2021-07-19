package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 entity 테스트")
class MenuTest {

    private Menu 메뉴;
    @BeforeEach
    void setUp() {
        메뉴 = new Menu(1L, "짜장면 탕수육 메뉴", new BigDecimal(19000), 1L);
    }

    @Test
    void 메뉴_entity_에_메뉴_상품_추가() {
        MenuProduct menuProduct = new MenuProduct(1L, 1);
        메뉴.addMenuProduct(menuProduct);
        assertThat(메뉴.getMenuProducts().products().size()).isEqualTo(1);
    }
}
