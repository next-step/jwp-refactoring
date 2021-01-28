package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
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
        Product productRequest = PRODUCT_REQUEST_생성("파스타", 12_000);

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
        Product productRequest = PRODUCT_REQUEST_생성("파스타", null);

        //when
        //then
        assertThatThrownBy(() -> {
            Product createdProduct = PRODUCT_생성_테스트(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("product group 리스트 조회")
    void product_show_test() {
        //given
        Product product1 = PRODUCT_생성_테스트(PRODUCT_REQUEST_생성("까르보나라", 12_000));
        Product product2 = PRODUCT_생성_테스트(PRODUCT_REQUEST_생성("알리오올리오", 12_000));

        //when
        List<Product> list = productService.list();

        //then
        Assertions.assertAll(() -> {
            List<String> collect = list.stream().map(Product::getName).collect(Collectors.toList());
            List<String> products = Arrays.asList(product1.getName(), product2.getName());
            assertThat(collect).containsAll(products);
        });

    }

    private Product PRODUCT_생성_테스트(Product productRequest) {
        return productService.create(productRequest);
    }

    private Product PRODUCT_REQUEST_생성(String name, Integer price) {
        Product product = new Product();
        product.setName(name);
        if (price != null) {
            product.setPrice(BigDecimal.valueOf(price));
        }
        return product;
    }

}
