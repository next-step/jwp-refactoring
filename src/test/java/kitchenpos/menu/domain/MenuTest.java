package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

class MenuTest {

    @DisplayName("메뉴 생성 시 메뉴 상품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotIncludeMenuProducts() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_생성("메뉴 상품이 null 인 메뉴", 1_000, null, null));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_생성("메뉴 상품이 null 인 메뉴", 1_000, null, 메뉴_상품_목록_생성(Collections.emptyList())));
    }

    @DisplayName("메뉴 생성 시 메뉴 상품 목록의 전체 가격과 메뉴의 가격이 일치하지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotMatchedTotalMenuProductPriceAndMenuPrice() {
        // given
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(상품_생성("상품", 가격_생성(1_000)), 수량_생성(2L));
        MenuProducts 메뉴_상품_목록 = 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품));

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_생성("메뉴", 1_000, null, 메뉴_상품_목록));
    }

    @DisplayName("정상 상태의 메뉴 생성 시 메뉴 객체가 생성되어야 한다")
    @Test
    void createMenuTest() {
        // given
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(상품_생성("상품", 가격_생성(1_000)), 수량_생성(1L));
        MenuProducts 메뉴_상품_목록 = 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품));

        // then
        assertThatNoException().isThrownBy(() -> 메뉴_생성("메뉴", 1_000, null, 메뉴_상품_목록));
    }
}