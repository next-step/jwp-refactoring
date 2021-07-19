package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 entity 테스트")
class MenuProductTest {

    private Menu 짜장면_메뉴;

    @BeforeEach
    void setUp() {
        짜장면_메뉴 = new Menu(1L, "짜장면", new Price(7000), 1L);
    }

    @Test
    void 메뉴_상품_entity에_메뉴_객체_추가() {
        MenuProduct menuProduct = new MenuProduct(1L, 1);

        menuProduct.withMenu(짜장면_메뉴);
        assertThat(menuProduct.getMenu().getId()).isEqualTo(짜장면_메뉴.getId());
        assertThat(menuProduct.getMenu().getName()).isEqualTo(짜장면_메뉴.getName());
        assertThat(menuProduct.getMenu().getPrice()).isEqualTo(짜장면_메뉴.getPrice());
        assertThat(menuProduct.getMenu().getMenuGroupId()).isEqualTo(짜장면_메뉴.getMenuGroupId());
    }
}
