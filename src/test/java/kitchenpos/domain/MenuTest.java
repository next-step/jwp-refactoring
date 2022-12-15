package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.ProductQuantityPair;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.InvalidPriceException;

class MenuTest {

    @DisplayName("[메뉴 등록] 가격이 비어있거나 0 미만인 경우 메뉴를 등록할 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Menu(null, "test", new MenuGroup(), Collections.emptyList()))
            .isInstanceOf(InvalidPriceException.class);
        assertThatThrownBy(() -> new Menu(BigDecimal.valueOf(-100), "test", new MenuGroup(), Collections.emptyList()))
            .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("[메뉴 등록] 가격은 메뉴상품 총금액합보다 작아야한다")
    @Test
    void test2() {
        assertThatThrownBy(
            () -> {
                BigDecimal price = BigDecimal.valueOf(100L);
                Product product = new Product("test", price);
                new Menu(price.add(BigDecimal.valueOf(10L)), "test", new MenuGroup(),
                    Collections.singletonList(new ProductQuantityPair(product, 1L)));
            })
            .isInstanceOf(InvalidMenuPriceException.class);
    }

}
