package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.메뉴_상품_생성;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("메뉴 상품의 가격은 상품 가격 * 수량 이어야 한다")
    @Test
    void menuProductTotalPriceTest() {
        // given
        Price 상품_가격 = 가격_생성(1_000);
        Quantity 수량 = 수량_생성(3L);
        Product 상품 = 상품_생성("상품", 상품_가격);
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(상품, 수량);

        // when
        Price 전체_가격 = 메뉴_상품.getTotalPrice();

        // then
        assertThat(전체_가격).isEqualTo(가격_생성(1_000 * 3));
    }
}