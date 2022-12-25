package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MenuProductTest {
    @DisplayName("상품 가격 계산 테스트")
    @Test
    void getPriceTest() {
        MenuGroup 메뉴그룹 = new MenuGroup("맛있는 메뉴");
        Menu 햄버거세트 = new Menu("햄버거세트", BigDecimal.valueOf(12000), 메뉴그룹);
        Product 햄버거 = new Product("햄버거", BigDecimal.valueOf(5000));
        MenuProduct 메뉴상품 = 메뉴상품_생성(null, 햄버거, 2);

        assertThat(메뉴상품.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }

    public static MenuProduct 메뉴상품_생성(Long seq, Product product, long quantity) {
        return new MenuProduct(seq, product, quantity);
    }
}