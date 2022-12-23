package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 생성시_상품가격이_null일경우_예외발생() {
        assertThatThrownBy(() -> createProduct("이름", null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_상품가격이_0보다작을경우_예외발생() {
        assertThatThrownBy(() -> createProduct("이름", BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_정상적인상품일경우_생성한상품반환() {
        final Product product = createProduct("아이패드", BigDecimal.ONE);
        assertAll(
            ()-> assertThat(product.getName()).isEqualTo("아이패드"),
            () -> assertThat(product.getPrice()).isEqualTo(new BigDecimal("1.00"))
        );
    }

    @Test
    void 조회시_존재하는상품목록반환() {
        assertThat(productService.list()).isNotEmpty();
    }

    private Product createProduct(String name, BigDecimal price) {
        return productService.create(new Product(name, price));
    }

}
