package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

public class MenuProductTest {
    @Test
    void 메뉴_상품의_가격을_구한다() {
        // given
        Long 수량 = 3L;
        Long 순살치킨_가격 = 20_000L;
        Product product = new Product(new Name("순살치킨"), new Price(BigDecimal.valueOf(순살치킨_가격)));
        MenuProduct menuProduct = new MenuProduct(new Quantity(수량), product);

        // when
        Price calculatePrice = menuProduct.calculatePrice();

        // then
        assertThat(calculatePrice.value()).isEqualTo(BigDecimal.valueOf(수량 * 순살치킨_가격));
    }
}
