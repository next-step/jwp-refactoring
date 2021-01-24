package kitchenpos.menu.application;

import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void cleanup() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("상품 등록")
    @Test
    void create() {
        ProductRequest productRequest = new ProductRequest("후라이드치킨", BigDecimal.valueOf(10000));
        ProductResponse actual = productService.create(productRequest);

        assertThat(actual.getPrice()).isEqualTo(productRequest.getPrice());
        assertThat(actual.getName()).isEqualTo(productRequest.getName());
    }

    @DisplayName("상품 값이 없거나 0인 경우 예외")
    @Test
    void validZero() {
        ProductRequest productRequest = new ProductRequest("후라이드치킨", BigDecimal.valueOf(-1));

        Assertions.assertThatThrownBy(() -> {
            productService.create(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
