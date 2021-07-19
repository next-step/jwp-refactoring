package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 반환 객체 테스트")
class MenuResponseTest {

    private MenuProduct 짜장면_메뉴;
    private MenuProduct 탕수육_메뉴;

    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        메뉴 = new Menu(1L, "짜장면 탕수육 메뉴", new BigDecimal(19000), 1L);
        짜장면_메뉴 = new MenuProduct(1L, 메뉴, 1L, new Quantity(1));
        탕수육_메뉴 = new MenuProduct(2L, 메뉴, 2L, new Quantity(1));
    }

    @Test
    void 메뉴_entity_객체를_이용하여_메뉴_반환_객체_생성() {
        메뉴.addMenuProduct(짜장면_메뉴);
        메뉴.addMenuProduct(탕수육_메뉴);
        MenuResponse expected = MenuResponse.of(메뉴);
        assertThat(expected.getId()).isEqualTo(메뉴.getId());
        assertThat(expected.getName()).isEqualTo(메뉴.getName());
        assertThat(expected.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
        assertThat(expected.getPrice()).isEqualTo(메뉴.getPrice().price());
        assertThat(expected.getMenuProductResponses().size()).isEqualTo(2);
    }
}
