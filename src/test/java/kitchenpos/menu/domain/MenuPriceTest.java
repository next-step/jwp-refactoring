package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuPriceTest {
    @Test
    @DisplayName("Menu Price 생성")
    void create() {
        // when
        final MenuPrice price = MenuPrice.of(BigDecimal.valueOf(1_000));
        // then
        assertThat(price).isInstanceOf(MenuPrice.class);
    }

    @Test
    @DisplayName("Menu Price 최솟값 오류")
    void createException() {
        // when
        final MenuPrice price = MenuPrice.of(BigDecimal.valueOf(1_000));
        // then
        assertThat(price).isInstanceOf(MenuPrice.class);
    }

}
