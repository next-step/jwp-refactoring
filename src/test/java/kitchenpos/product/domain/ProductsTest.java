package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품들")
class ProductsTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Products.singleton(
                Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.ONE))));
    }

    @Test
    @DisplayName("상품 리스트 필수")
    void instance_nullProducts_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Products.from(null))
            .withMessage("주문 테이블 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("상품 리스트에 null이 포함될 수 없음")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Products.from(Collections.singletonList(null)))
            .withMessageEndingWith("null이 포함될 수 없습니다.");
    }

    @Test
    @DisplayName("가격 합산")
    void sumPrice() {
        //given
        Product 후라이드치킨 = Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.TEN));
        Product 양념치킨 = Product.of(Name.from("양념치킨"), Price.from(BigDecimal.TEN));

        //when
        Price sumPrice = Products.from(Arrays.asList(후라이드치킨, 양념치킨)).sumPrice();

        //then
        assertThat(sumPrice).isEqualTo(Price.from(BigDecimal.valueOf(20)));
    }

}
