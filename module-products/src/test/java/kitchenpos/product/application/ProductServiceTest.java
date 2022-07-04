package kitchenpos.product.application;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product 짬뽕;
    private Product 짜장;

    @BeforeEach
    void before() {
        짬뽕 = productRepository.save(new Product("짬뽕", BigDecimal.valueOf(1000)));
        짜장 = productRepository.save(new Product("짜장", BigDecimal.valueOf(1000)));
    }

    @Test
    @DisplayName("상품을 생성 할 수 있다.")
    void createTest() {
        //given
        ProductRequest 저장할_상품 = ProductRequest.of("짬뽕", BigDecimal.valueOf(1000));

        //when
        ProductResponse productResponse = productService.create(저장할_상품);
        //then
        assertThat(productResponse).isNotNull();
    }

    @Test
    @DisplayName("상품 목록을 조회 한다.")
    void listTest() {

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products).hasSize(2);
    }
}
