package kitchenpos.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    private Product 양념치킨;
    private Product 후라이드치킨;
    private ProductRequest 양념치킨_리퀘스트;
    private ProductRequest 후라이드치킨_리퀘스트;

    @BeforeEach
    void setUp() {
        양념치킨 = new Product("양념치킨", BigDecimal.valueOf(19000));
        양념치킨_리퀘스트 = new ProductRequest("양념치킨", BigDecimal.valueOf(19000));

        후라이드치킨 = new Product("후라이드치킨", BigDecimal.valueOf(18000));
        후라이드치킨_리퀘스트 = new ProductRequest("후라이드치킨", BigDecimal.valueOf(18000));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        //when
        ProductResponse createdProduct = productService.create(양념치킨_리퀘스트);

        //then
        assertThat(createdProduct.getPrice()).isEqualTo(양념치킨.getPrice().getValue());
    }

    @Test
    @DisplayName("상품가격이 0원 미만일 경우 상품 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero() {
        //given
        양념치킨_리퀘스트 = new ProductRequest("양념치킨", BigDecimal.valueOf(-1));

        //when
        assertThatThrownBy(() -> productService.create(양념치킨_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() {
        //when
        List<ProductResponse> foundProducts = productService.list();

        //then
        assertThat(foundProducts.size()).isGreaterThan(0);
    }
}