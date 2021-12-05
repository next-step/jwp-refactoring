package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 상품을 등록할 수 있다
 * - 상품의 가격이 올바르지 않으면 등록할 수 없다
 *     - 상품의 가격은 0 원 이상이어야 한다
 * - 상품의 목록을 조회할 수 있다
 */
class ProductServiceTest {

    private static final String 상품_이름 = "강정치킨";
    private static final BigDecimal 상품_가격 = new BigDecimal(17_000);
    private static final Product 상품 = 상품_생성(상품_이름, 상품_가격);

    ProductDao productDao;
    ProductService productService;

    @BeforeEach
    void setUp() {
        productDao = new InMemoryProductDao();
        productService = new ProductService(productDao);
    }

    @Test
    void create_상품을_등록할_수_있다() {
        Product savedProduct = productService.create(상품);
        assertAll(
                () -> assertThat(savedProduct.getName()).isEqualTo(상품_이름),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(상품_가격)
        );
    }

    private static Product 상품_생성(String name, BigDecimal price) {
        Product product = new Product();
        product.setId(1L);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}