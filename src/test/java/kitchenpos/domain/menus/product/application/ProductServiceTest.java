package kitchenpos.domain.menus.product.application;

import kitchenpos.domain.menus.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품을 관리한다")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    private String name = "고구마 피자";

    @DisplayName("상품을 등록할수 있다.")
    @Test
    void createTest() {
        // given
        Product product = new Product(name, BigDecimal.valueOf(10000));

        // when
        Product actualProduct = productService.create(product);

        // then
        assertThat(actualProduct).isNotNull();
        assertThat(actualProduct.getName()).isEqualTo(name);
    }

    @DisplayName("상품들을 조회할수 있다.")
    @Test
    void selectTest() {
        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).isNotNull();
    }
}