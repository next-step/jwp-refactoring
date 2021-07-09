package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository.save(new Product("테스트제품1", BigDecimal.valueOf(1000L)));
        productRepository.save(new Product("테스트제품2", BigDecimal.valueOf(2000L)));
    }


    @DisplayName("제품 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        ProductRequest request = new ProductRequest("제품", BigDecimal.valueOf(1000L));

        // when
        ProductResponse productResponse = productService.create(request);

        // then
        assertThat(productResponse).isNotNull()
                                   .extracting(ProductResponse::getName)
                                   .isEqualTo("제품");
    }

    @DisplayName("전체 제품 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        assertThat(productService.list()).isNotEmpty()
                                         .hasSizeGreaterThanOrEqualTo(2);
    }
}
