package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.ProductQuantityPair;
import kitchenpos.exception.InvalidMenuPriceException;

class MenuTest {

    @DisplayName("[메뉴 등록] 가격은 메뉴상품 총금액합보다 작아야한다")
    @Test
    void test2() {
        assertThatThrownBy(
            () -> {
                Price price = new Price(BigDecimal.valueOf(100L));
                Product product = new Product(new Name("test"), price);
                new Menu(price.add(new Price(BigDecimal.valueOf(10L))), new Name("test"), new MenuGroup(),
                    Collections.singletonList(new ProductQuantityPair(product, new Quantity(1L))));
            })
            .isInstanceOf(InvalidMenuPriceException.class);
    }

}
