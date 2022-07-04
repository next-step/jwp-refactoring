package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    Product 스낵랩;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product("스낵랩", BigDecimal.valueOf(3000));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품 정상 등록")
    public void saveSuccess() {
        assertThat(productService.create(스낵랩).getId()).isNotNull();
    }

    @Test
    public void list() {
        Product 맥모닝 = new Product("맥모닝", BigDecimal.valueOf(4000));
        productService.create(스낵랩);
        productService.create(맥모닝);

        assertThat(productService.list()).hasSize(2);
    }
}