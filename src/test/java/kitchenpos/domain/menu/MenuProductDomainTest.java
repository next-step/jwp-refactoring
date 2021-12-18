package kitchenpos.domain.menu;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

public class MenuProductDomainTest {
    @DisplayName("주어진 개수를 곱한 상품가격이 계산된다.")

    @CsvSource({"2,1800,3600", "3,1500,4500"})
    @ParameterizedTest(name ="[{index}] 주어진 상품 개수는 [{0}] 가격은 [{1}] 예상되는 가격은 [{2}]")
    void calculate_productPriceWithQuantity(int quantity, int productPrice, int expectedPrice) {
        // given
        MenuProduct menuProduct = MenuProduct.of(Product.of("메뉴", Price.of(productPrice)), quantity);

        // when
        Price calculatedPrice = menuProduct.calculatePrice();

        // then
        Assertions.assertThat(calculatedPrice).isEqualTo(Price.of(expectedPrice));
    }
}
