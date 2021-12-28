package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 컬렉션 도메인 테스트")
public class MenuProductsTest {
    @DisplayName("메뉴 상품 컬렉션을 생성한다.")
    @Test
    void 메뉴_상품_컬렉션_생성() {
        // given
        MenuProduct 양념치킨_메뉴_상품 = MenuProduct.of(1L, Quantity.of(2L));
        MenuProduct 콜라_메뉴_상품 = MenuProduct.of(2L, Quantity.of(3L));
        List<MenuProduct> menuProductList = new ArrayList<>(Arrays.asList(양념치킨_메뉴_상품, 콜라_메뉴_상품));

        // when
        MenuProducts menuProducts = MenuProducts.of(menuProductList);

        // then
        assertThat(menuProducts.asList().size()).isEqualTo(2);
    }
}
