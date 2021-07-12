package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import kitchenpos.error.InvalidRequestException;
import kitchenpos.product.domain.Product;

@DisplayName("상품 일급 컬렉션 테스트")
class ProductsTest {

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product(1L, "순대", new Price(100));
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        Products products = new Products(Arrays.asList(product), 1);
        // then
        assertThat(products).isNotNull();
    }

    @DisplayName("생성 실패 - 요청 개수가 다름")
    @Test
    void createFailedByRequestSize() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Products(Arrays.asList(product), 2))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("전체 금액 구하기")
    @Test
    void totalPrice() {
        // given
        Products products = new Products(Arrays.asList(product), 1);
        Map<Long, Quantity> quantityMap = new HashMap<>();
        quantityMap.put(1L, new Quantity(2L));
        // when
        Price totalPrice = products.totalPrice(new Quantities(quantityMap, 1));
        // then
        assertThat(totalPrice.amountToInt()).isEqualTo(200);
    }
}