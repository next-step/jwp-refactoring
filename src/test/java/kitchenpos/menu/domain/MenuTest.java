package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.order.domain.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private Menu menu;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("menu group");
        menu = new Menu.Builder("메뉴이름")
                .setId(1L)
                .setPrice(Price.of(10_000L))
                .setMenuGroup(menuGroup)
                .build();
    }

    @Test
    @DisplayName("메뉴가 같은지 검증")
    void verifyEqualsMenu() {
        assertThat(menu).isEqualTo(new Menu.Builder("메뉴이름")
                .setId(1L)
                .setPrice(Price.of(10_000L))
                .setMenuGroup(menuGroup)
                .build());
    }

    @Test
    @DisplayName("메뉴에 메뉴 제품들이 잘 들어가는지 검증")
    void addMenuProduct() {
        menu.addProduct(1L, Quantity.of(1L));

        assertThat(menu.products().get()).hasSize(1);
    }
}
