package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {
    @DisplayName("메뉴에 상품을 추가한다.")
    @Test
    void 메뉴_상품_추가() {
        MenuProduct 양념치킨_메뉴_상품 = MenuProduct.of(1L, Quantity.of(2L), BigDecimal.valueOf(5000));
        MenuProduct 맥주_메뉴_상품 = MenuProduct.of(2L, Quantity.of(4L), BigDecimal.valueOf(50000));
        Menu 양념치킨_세트 = Menu.of("양념치킨_세트", Price.of(BigDecimal.valueOf(55000)), 1L, MenuProducts.of(Arrays.asList(양념치킨_메뉴_상품, 맥주_메뉴_상품)));


        assertThat(양념치킨_세트.getMenuProducts()).containsExactly(양념치킨_메뉴_상품, 맥주_메뉴_상품);
    }
}
