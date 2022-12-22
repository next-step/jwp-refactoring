package kitchenpos.product.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.vo.Name;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static kitchenpos.common.vo.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.common.vo.Price.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 서비스")
class ProductServiceTest extends ServiceTest {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        productService = new ProductService(productRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"상품A"})
    @DisplayName("상품 생성")
    void create(String name) {
        ProductResponse product = productService.create(new ProductCreateRequest(name, BigDecimal.ONE));
        assertAll(
                () -> assertEquals(0, product.getPrice().compareTo(BigDecimal.ONE)),
                () -> assertThat(product.getName()).isEqualTo(new Name(name))
        );
    }

    @DisplayName("상품 생성 / 가격을 필수로 갖는다.")
    @Test
    void create_fail_priceNull() {
        assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 생성 / 가격은 0원보다 작을 수 없다.")
    @Test
    void create_fail_minimumPrice() {
        assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        productService.create(new ProductCreateRequest("상품A", BigDecimal.ONE));
        assertThat(productService.list()).hasSize(1);
    }
}

