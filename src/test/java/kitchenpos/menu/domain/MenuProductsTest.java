package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.메뉴_상품_목록_생성;
import static kitchenpos.menu.MenuGenerator.메뉴_상품_생성;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @DisplayName("메뉴 상품 리스트에 존재하는 총 가격은 (상품 가격 * 수량) 와 일치해야 한다")
    @Test
    void MenuProductsTotalPriceTest() {
        // given
        Product 상품1 = 상품_생성("상품 1", 가격_생성(1_000));
        Product 상품2 = 상품_생성("상품 2", 가격_생성(2_000));
        Quantity 수량 = 수량_생성(5L);
        MenuProduct 메뉴_상품1 = 메뉴_상품_생성(상품1, 수량);
        MenuProduct 메뉴_상품2 = 메뉴_상품_생성(상품2, 수량);
        MenuProducts 메뉴_상품_목록 = 메뉴_상품_목록_생성(Arrays.asList(메뉴_상품1, 메뉴_상품2));

        // when
        boolean 메뉴_상품_목록_전체_가격_불일치_여부1 = 메뉴_상품_목록.isNotSameTotalPriceByPrice(가격_생성((1_000 * 5) + (2_000 * 5)));
        boolean 메뉴_상품_목록_전체_가격_불일치_여부2 = 메뉴_상품_목록.isNotSameTotalPriceByPrice(가격_생성((1_000 * 5) + (2_000 * 5) - 1));

        // then
        assertThat(메뉴_상품_목록_전체_가격_불일치_여부1).isFalse();
        assertThat(메뉴_상품_목록_전체_가격_불일치_여부2).isTrue();
    }
}