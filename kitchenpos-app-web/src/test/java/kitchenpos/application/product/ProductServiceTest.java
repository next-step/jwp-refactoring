package kitchenpos.application.product;

import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.Product;
import kitchenpos.application.valueobject.Price;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 기능 테스트")
@SpringBootTest
@Transactional
public class ProductServiceTest {
    private ProductRequest productRequest;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"초코파이", "가나파이"})
    public void create(String name) throws Exception {
        // given
        final BigDecimal price = new BigDecimal(1000);
        productRequest = new ProductRequest(name, price);

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productRequest.getName());
        assertThat(productResponse.getPrice().longValue()).isEqualTo(productRequest.getPrice().longValue());
    }

    @Test
    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    public void createFail() throws Exception {
        // when then
        assertThatThrownBy(() -> {
            new Product("실패상품", Price.of(-1));
            create("김밥");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        // given
        create("떡볶이");
        create("순대");

        // when
        List<ProductResponse> products = productService.findAll();

        // then
        assertThat(products).isNotEmpty();
    }
}