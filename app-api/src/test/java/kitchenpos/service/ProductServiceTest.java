package kitchenpos.service;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.service.product.ProductService;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("상품 관련 테스트")
@Transactional
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 저장 테스트")
    @Test
    void saveProduct() {
        ProductResponse product = productService.save(new Product("치킨", new Price(18000)));

        assertThat(product.getId()).isNotNull();
    }

    @DisplayName("상품 조회 테스트")
    @Test
    void findProducts() {
        productService.save(new Product("치킨", new Price(18000)));
        productService.save(new Product("순대", new Price(5000)));

        List<ProductResponse> products = productService.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting("name").containsExactly("치킨", "순대");
    }
}
