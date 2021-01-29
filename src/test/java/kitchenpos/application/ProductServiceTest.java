package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(12_000);
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("product 생성")
    void product_create_test() {
        //given
        ProductRequest productRequest = PRODUCT_REQUEST_생성("파스타", DEFAULT_VALUE);

        //when
        ProductResponse createdProduct = PRODUCT_생성_테스트(productRequest);

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
        ProductRequest productRequest = PRODUCT_REQUEST_생성("파스타", null);

        //when
        //then
        assertThatThrownBy(() -> {
            ProductResponse createdProduct = PRODUCT_생성_테스트(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("product group 리스트 조회")
    void product_show_test() {
        //given
        ProductResponse product1 = PRODUCT_생성_테스트(PRODUCT_REQUEST_생성("까르보나라", DEFAULT_VALUE));

        ProductResponse product2 = PRODUCT_생성_테스트(PRODUCT_REQUEST_생성("알리오올리오", DEFAULT_VALUE));

        //when
        List<ProductResponse> list = productService.list();

        //then
        assertThat(list)
            .extracting(ProductResponse::getName)
            .containsExactly(product1.getName(), product2.getName());

    }

    private ProductResponse PRODUCT_생성_테스트(ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    private ProductRequest PRODUCT_REQUEST_생성(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

}
