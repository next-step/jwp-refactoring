package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 일급 컬랙션")
class MenuProductsTest {
    private static final Product 후라이드 = Product.of("후라이드", 16000);
    private static final MenuProduct 단일_후라이드_메뉴_상품 = MenuProduct.of(후라이드, 1L);
    private static final MenuProduct 트리플_후라이드_메뉴_상품 = MenuProduct.of(후라이드, 3L);

    public static final List<MenuProduct> 메뉴_상품_리스트 = Arrays.asList(단일_후라이드_메뉴_상품, 트리플_후라이드_메뉴_상품);

    private MenuProducts 메뉴_상품들;

    @BeforeEach
    void setUp() {
        메뉴_상품들 = new MenuProducts(메뉴_상품_리스트);
    }

    @DisplayName("메뉴 상품을 추가할 수 있다.")
    @Test
    void 메뉴_상품_추가() {
        메뉴_상품들.add(MenuProduct.of(후라이드, 2L));

        assertThat(메뉴_상품들.value()).hasSize(3);
    }

    @DisplayName("전체 메뉴 상품 금액의 합을 구할 수 있다.")
    @Test
    void 전체_메뉴_상품_금액의_합() {
        assertThat(메뉴_상품들.totalAmount()).isEqualTo(Price.from(64000));
    }
}
