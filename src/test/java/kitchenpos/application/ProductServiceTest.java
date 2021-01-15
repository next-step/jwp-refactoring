package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("제품 서비스")
public class ProductServiceTest extends ServiceTestBase {
    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @BeforeEach
    void setUp() {
        setUpProduct();
    }

    @DisplayName("제품을 등록한다")
    @Test
    void createProduct() {
        Product savedProduct = productService.create(product);
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("가격이 부적합한 제품을 등록한다")
    @ParameterizedTest
    @MethodSource
    void createProductWithIllegalArguments(BigDecimal price) {
        product.setPrice(price);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    private static Stream<Arguments> createProductWithIllegalArguments() {
        return Stream.of(
                Arguments.of((Object)null),
                Arguments.of(new BigDecimal(-1))
        );
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<Product> products = productService.list();
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0)).isEqualTo(product);
    }
}
