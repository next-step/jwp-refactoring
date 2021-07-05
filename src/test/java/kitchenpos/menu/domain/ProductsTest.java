package kitchenpos.menu.domain;

import kitchenpos.common.domian.Price;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("상품 일급 컬렉션 테스트")
class ProductsTest {

    Product product;

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
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.NOT_EQUAL_REQUEST_SIZE.message());
    }
}