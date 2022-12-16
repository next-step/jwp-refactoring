package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 목록 일급 컬렉션 테스트")
public class MenuProductBagTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final MenuProductBag 메뉴_쌍품_목록 = MenuProductBag.from(
                Arrays.asList(
                        메뉴_상품(상품("상품"), 1),
                        메뉴_상품(상품("상품2"), 1)));
        //when, then:
        assertThat(메뉴_쌍품_목록.getMenuProductList()).hasSize(2);
    }

    @DisplayName("상품 총 금액 조회 성공")
    @Test
    void 상품_총_금액_조회_성공() {
        //gvien:
        final Product 상품 = 상품("상품", BigDecimal.valueOf(20));
        final int 상품_수량 = 1;
        final Product 상품2 = 상품("상품2", BigDecimal.valueOf(10));
        final int 상품2_수량 = 2;
        final MenuProductBag 메뉴_상품_목록 = MenuProductBag.from(
                Arrays.asList(
                        메뉴_상품(상품("상품", BigDecimal.valueOf(20)), 1),
                        메뉴_상품(상품("상품2", BigDecimal.valueOf(10)), 2)));
        final Price expected = Price.from(
                BigDecimal.valueOf((상품.getPrice().intValue() * 상품_수량) + ((long) 상품2.getPrice().intValue() * 상품2_수량)));
        //when, then:
        assertThat(메뉴_상품_목록.totalPrice()).isEqualTo(expected);
    }
}
