package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 상품 컬렉션 도메인 테스트")
class MenuProductsTest {

    @Test
    @DisplayName("합계 금액을 반환한다.")
    void totalPrice() {
        // given
        Product product1 = new Product("후라이드치킨", new BigDecimal(16_000));
        Product product2 = new Product("양념치킨", new BigDecimal(17_000));
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(
                new MenuProduct(product1, 3), new MenuProduct(product2, 2)
        ));

        // when
        Price sum = menuProducts.totalPrice();

        // then
        assertThat(sum).isEqualTo(new Price(82_000));
    }
}
