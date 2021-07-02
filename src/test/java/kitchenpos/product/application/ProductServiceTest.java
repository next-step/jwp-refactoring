package kitchenpos.product.application;

import kitchenpos.common.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 기능 테스트")
@SpringBootTest
public class ProductServiceTest {
    private Product product1;
    private ProductRequest productRequest;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product1 = new Product("후라이드", Price.of(16000));
        productRequest = new ProductRequest(this.product1.getName(), this.product1.getPrice());
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    public void create() throws Exception {
        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(this.product1.getName());
        assertThat(productResponse.getPrice()).isEqualTo(this.product1.getPrice().getPrice());
    }

    @Test
    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    public void createFail() throws Exception {
        // when then
        assertThatThrownBy(() -> {
            new Product("실패상품", Price.of(-1));
            productService.create(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // when
        List<ProductResponse> products = productService.findAll();

        // then
        assertThat(products).isNotEmpty();
    }
}