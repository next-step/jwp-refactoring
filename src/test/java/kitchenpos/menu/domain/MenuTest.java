package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {

    private MenuGroup menuGroup;
    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("두마리메뉴");
        MenuProduct menuProduct1 = new MenuProduct(new Product("후라이드치킨", new BigDecimal(16_000)), 2);
        MenuProduct menuProduct2 = new MenuProduct(new Product("양념치킨", new BigDecimal(17_000)), 1);
        menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));
    }

    @Test
    @DisplayName("메뉴 상품들의 금액 합계와 다른 가격으로 메뉴를 생성하면 예외를 발생한다.")
    void createThrowException() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Menu("치킨", new BigDecimal(50_000), menuGroup.getId(), menuProducts))
                .withMessageMatching(Menu.MESSAGE_VALIDATE_PRICE);
    }
}
