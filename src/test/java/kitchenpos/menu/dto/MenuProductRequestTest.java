package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 요청 테스트")
class MenuProductRequestTest {

    @Test
    void 메뉴_상품_요청_객체를_이용하여_메뉴_상품_entity_생성() {
        Menu menu = new Menu(1L, "짜장면", new BigDecimal(7000), 1L);
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuProduct menuProduct = menuProductRequest.toMenuProduct(menu);
        assertThat(menuProduct).isEqualTo(new MenuProduct(menu, 1L, 1));
    }
}
