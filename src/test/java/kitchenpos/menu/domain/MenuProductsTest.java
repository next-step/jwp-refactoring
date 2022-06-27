package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Price;
import kitchenpos.helper.MenuProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품들 관련 Domain 단위 테스트")
class MenuProductsTest {

    @DisplayName("총 금액을 생성한다.")
    @Test
    void getTotalAmount() {
        //given
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(MenuProductFixtures.후라이드치킨_메뉴상품);
        menuProducts.add(MenuProductFixtures.양념치킨_메뉴);

        //when then
        Price totalPrice = menuProducts.getTotalPrice();
        assertThat(totalPrice.getPrice()).isEqualTo(32_000);
    }
}
