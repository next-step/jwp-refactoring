package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        Product product = productService.create(new Product("치킨", new BigDecimal("17000")));

        assertThat(product.getName()).isEqualTo("치킨");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("17000.00"));
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void productPriceGraterThanZero() {
        assertThatThrownBy(()-> productService.create(new Product("치킨", new BigDecimal(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void getProductFindAll() {
        List<Product> list = productService.list();

        assertThat(list)
                .extracting("name")
                .containsExactly("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }
}