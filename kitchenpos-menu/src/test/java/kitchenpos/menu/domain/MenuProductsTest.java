package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

public class MenuProductsTest {
    @Test
    void 전체_메뉴_상품의_총_합을_구한다() {
        // given
        Product 순살치킨_상품 = new Product(new Name("순살치킨"), new Price(BigDecimal.valueOf(20_000)));
        Product 간장치킨_상품 = new Product(new Name("간장치킨"), new Price(BigDecimal.valueOf(20_000)));
        MenuProduct 순살치킨 = new MenuProduct(new Quantity(1L), 순살치킨_상품);
        MenuProduct 간장치킨 = new MenuProduct(new Quantity(1L), 간장치킨_상품);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(순살치킨, 간장치킨));

        // when
        Price result = menuProducts.totalMenuPrice();

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(40_000));
    }
}
