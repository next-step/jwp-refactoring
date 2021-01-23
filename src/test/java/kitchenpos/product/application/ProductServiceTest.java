package kitchenpos.product.application;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // given
        ProductRequest product = new ProductRequest("사이다", BigDecimal.valueOf(1_000));

        // when
        ProductResponse response = productService.create(product);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("사이다");
    }

    @DisplayName("상품 등록 예외 - 상품 금액은 0보다 커야 한다.")
    @Test
    void create_exception1() {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> productService.create(new ProductRequest("사이다", BigDecimal.valueOf(-1))))
            .withMessage("상품 금액은 0보다 커야 한다.");
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<ProductResponse> list = productService.list();

        // then
        assertThat(list.size()).isGreaterThan(0);
    }

}
