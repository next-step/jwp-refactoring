package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("product 생성")
    void product_create_test() {
        //given
        Product productRequest = PRODUCT_REQUEST_생성("파스타");

        //when
        Product createdProduct = PRODUCT_생성_테스트(productRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdProduct.getId()).isNotNull();
            assertThat(createdProduct.getName()).isEqualTo(productRequest.getName());
        });
    }

    @Test
    @DisplayName("product의 price는 0 원 이상이어야 한다.")
    void product_create_price_null_test() {
        //given
        Product productRequest = PRODUCT_REQUEST_PRICE_NULL_생성("파스타");

        //when
        //then
        assertThatThrownBy(()->{
            Product createdProduct = PRODUCT_생성_테스트(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    private Product PRODUCT_생성_테스트(Product productRequest) {
        return productService.create(productRequest);
    }

    private Product PRODUCT_REQUEST_생성(String name) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(12_000));
        return product;
    }
    private Product PRODUCT_REQUEST_PRICE_NULL_생성(String name) {
        Product product = new Product();
        product.setName(name);

        return product;
    }
}
