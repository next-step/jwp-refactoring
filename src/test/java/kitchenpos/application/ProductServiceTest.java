package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 생성시 가격이 음수가 입력되는 경우 예외를 던진다.")
    void createFail() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.productService.create(new Product("후라이드", BigDecimal.valueOf(-1))));
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.productService.create(new Product("후라이드", null)));
    }

    @Test
    @DisplayName("상품이 정상적으로 생성된다.")
    void create() {
        Product product = new Product("후라이드", BigDecimal.valueOf(16000.00));

        Product createdProduct = this.productService.create(product);

        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getPrice().intValue()).isEqualTo(16000);
    }

    @Test
    @DisplayName("상품을 모두 조회한다.")
    void list() {
        Product product1 = new Product("후라이드", BigDecimal.valueOf(16000.00));
        Product product2 = new Product("양념치킨", BigDecimal.valueOf(16000.00));
        product1 = this.productService.create(product1);
        product2 = this.productService.create(product2);

        List<Product> products = this.productService.list();

        assertThat(products).containsExactly(product1, product2);
    }

}
