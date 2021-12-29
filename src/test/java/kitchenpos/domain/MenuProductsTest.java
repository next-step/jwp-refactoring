package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품들 관련 테스트")
class MenuProductsTest {

    private Product 상품;
    private MenuProduct 메뉴상품;
    private List<MenuProduct> 메뉴상품_목록;
    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private MenuProducts 메뉴상품들;

    @BeforeEach
    void setUp() {
        상품 = Product.of("상품", new BigDecimal("12000"));
        메뉴그룹 = MenuGroup.of("메뉴그룹");
        메뉴 = Menu.of("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹);
        메뉴상품 = MenuProduct.of(메뉴, 상품, 2L);
        메뉴상품_목록 = Collections.singletonList(메뉴상품);
        메뉴상품들 = MenuProducts.from(Collections.singletonList(메뉴상품));
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        final List<MenuProduct> actual = 메뉴상품_목록;

        assertThat(MenuProducts.from(actual)).isEqualTo(MenuProducts.from(actual));
    }

    @DisplayName("주어진 가격이 총 구성품 가격보다 높은지 확인할 수 있다")
    @Test
    void isOverPriceTest() {
        final Price price = Price.valueOf(50_000);

        final boolean result = 메뉴상품들.isOverPrice(price);

        assertThat(result).isTrue();
    }
}