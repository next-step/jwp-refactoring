package kitchenpos.application;

import kitchenpos.dao.InMemoryProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 상품을 등록할 수 있다
 * - 상품의 가격이 올바르지 않으면 등록할 수 없다
 *     - 상품의 가격은 0 원 이상이어야 한다
 * - 상품의 목록을 조회할 수 있다
 */
class ProductServiceTest {

    private static final String 상품_이름 = "강정치킨";
    private static final int 상품_가격 = 17_000;
    private static final ProductRequest 상품_요청 = ProductRequest.of(상품_이름, 상품_가격);

    ProductRepository productRepository;
    ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        productService = new ProductService(productRepository);
    }

    @Test
    void create_상품을_등록할_수_있다() {
        Product savedProduct = productService.create(상품_요청);
        assertAll(
                () -> assertThat(savedProduct.getName()).isEqualTo(상품_이름),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(상품_가격))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void create_상품의_가격이_올바르지_않으면_등록할_수_없다(int 유효하지_않은_상품_가격) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(ProductRequest.of(상품_이름, 유효하지_않은_상품_가격)));
    }

    @Test
    void list_상품의_목록을_조회할_수_있다() {
        productService.create(상품_요청);
        List<Product> products = productService.list();
        assertThat(products.size()).isEqualTo(1);
    }
}