package kitchenpos.menu.domain;

import static kitchenpos.helper.MenuProductFixtures.양념치킨_메뉴상품;
import static kitchenpos.helper.MenuProductFixtures.후라이드치킨_메뉴상품;
import static kitchenpos.helper.ProductFixtures.양념치킨_상품;
import static kitchenpos.helper.ProductFixtures.후라이드치킨_상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품들 관련 Domain 단위 테스트")
class MenuProductsTest {

    @DisplayName("총 금액을 생성한다.")
    @Test
    void getTotalAmount() {
        //given
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(후라이드치킨_메뉴상품);
        menuProducts.add(양념치킨_메뉴상품);
        //when
        Price totalPrice = menuProducts.getTotalPrice(new Products(Arrays.asList(후라이드치킨_상품, 양념치킨_상품)));

        //then
        assertThat(totalPrice.getPrice()).isEqualTo(32_000);
    }
}
