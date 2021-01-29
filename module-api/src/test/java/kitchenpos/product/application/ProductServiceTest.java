package kitchenpos.product.application;

import kitchenpos.advice.exception.PriceException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        ProductRequest 뿌링클 = new ProductRequest("뿌링클", BigDecimal.valueOf(18000));

        Product result = productService.create(뿌링클);

        assertThat(result.getName()).isEqualTo("뿌링클");
    }

    @DisplayName("상품을 등록할 때 가격이 없거나 0원보다 작으면 익셉션 발생")
    @Test
    void createException() {
        ProductRequest 뿌링클 = new ProductRequest("뿌링클", null);

        assertThatThrownBy(() -> productService.create(뿌링클))
                .isInstanceOf(PriceException.class);
    }

    @DisplayName("상품들을 조회한다")
    @Test
    void list() {
        List<Product> products = productService.list();
        assertThat(products.size()).isGreaterThanOrEqualTo(1);
    }
}