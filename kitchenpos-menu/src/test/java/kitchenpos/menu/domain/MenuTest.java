package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.domain.Product;

class MenuTest {

    @DisplayName("[메뉴 등록] 가격은 메뉴상품 총금액합보다 작아야한다")
    @Test
    void test2() {
        assertThatThrownBy(
            () -> {
                Price price = new Price(BigDecimal.valueOf(100L));
                Product product = new Product(new Name("test"), price);
                new Menu(price.add(new Price(BigDecimal.valueOf(10L))), new Name("test"), new MenuGroup(),
                    Collections.singletonList(new MenuProduct(product, new Quantity(1L))));
            })
            .isInstanceOf(InvalidMenuPriceException.class);
    }

}
