package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("상품 Service 테스트")
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    ProductService productService;

    private Product 치킨;
    private Product 피자;

    @BeforeEach
    void setUp() {
        치킨 = createProduct( "치킨", 15000);
        피자 = createProduct("피자", 20000);
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록(){
        //when
        ProductResponse savedProduct = productService.create(ProductRequest.of(치킨.getName(), 치킨.getPrice()));

        //then
        Assertions.assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(치킨.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(치킨.getPrice())
        );
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        //given
        ProductResponse savedProduct = productService.create(ProductRequest.of(치킨.getName(), 치킨.getPrice()));

        //when
        List<ProductResponse> list = productService.list();

        //then
        assertThat(list).contains(savedProduct);
    }
}