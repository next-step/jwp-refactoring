package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void 메뉴와의_연관관계를_설정할_수_있어야_한다() {
        // given
        final MenuProduct given = new MenuProduct(1L, new Quantity(10));

        // when
        final Menu menu = new Menu(1L, "menu", new Price(15000L), 1L);
        given.relateToMenu(menu);

        // then
        assertThat(given.getMenu()).isEqualTo(menu);
    }

    @Test
    void 이미_연관관계가_있는_상태라면_메뉴와의_연관관계_설정_시_에러가_발생해야_한다() {
        // given
        final Menu menu = new Menu(1L, "menu", new Price(15000L), 1L);
        final MenuProduct given = new MenuProduct(1L, menu, 1L, new Quantity(10));

        // when and then
        assertThatThrownBy(() -> given.relateToMenu(menu))
                .isInstanceOf(IllegalStateException.class);
    }
}
