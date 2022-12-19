package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void 상품가격이_null일경우_에러발생() {
        assertThatThrownBy(() -> productService.create(new Product("이름", null)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품가격이_0원이하일경우_에러발생() {
        assertThatThrownBy(() -> productService.create(new Product("이름", BigDecimal.valueOf(-1))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품가격이_0원이상일경우_상품반환() {
        assertThat(productService.create(new Product("아이패드", BigDecimal.ONE)))
            .isEqualTo(new Product("아이패드", BigDecimal.ONE));
    }

    @Test
    void 상품목록_조회() {
        assertThat(productService.list()).isNotEmpty();
    }

}
