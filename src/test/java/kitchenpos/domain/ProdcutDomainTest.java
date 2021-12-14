package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kitchenpos.domain.product.Product;

@DisplayName("주문도메인에 대한")
public class ProdcutDomainTest {
    @DisplayName("주어진 개수를 곱한 상품가격이 계산된다.")
    @CsvSource({"0,0", "2,4600", "7,16100"})
    @ParameterizedTest(name ="[{index}] 주어진 상품 개수는 [{0}] 예상되는 가격은 [{1}]")
    void calculate_ProductPrice(int quantity, int expectedPrice) {
        // given
        Product product = Product.of("신상품", Price.of(2_300));

        // when
        Price price = product.calculatePriceWithQuantity(quantity);

        // then
        Assertions.assertThat(price).isEqualTo(Price.of(expectedPrice));
    }
}
