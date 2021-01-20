package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductService productService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setName("순대국맛 양념 치킨");
        mockProduct.setPrice(new BigDecimal(18000));
    }

    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        Product result = productService.create(mockProduct);

        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("상품 등록 실패")
    @Test
    void createProductFail() {
        mockProduct.setPrice(null);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(mockProduct));
    }

    @DisplayName("상품 조회")
    @Test
    void findProduct() {
        List<Product> result = productService.list();

        assertThat(result).isNotEmpty();
    }
}
