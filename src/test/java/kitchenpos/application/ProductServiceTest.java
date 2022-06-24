package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품이 정상적으로 생성된다.")
    void create() {
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(16000.00));

        ProductResponse createdProduct = this.productService.create(productRequest);

        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getPrice().intValue()).isEqualTo(16000);
    }

    @ParameterizedTest(name = "가격이 {0}일 경우")
    @DisplayName("상품 생성시 가격이 음수가 입력되는 경우 예외를 던진다.")
    @NullSource
    @ValueSource(strings = {"-1"})
    void createFail(BigDecimal price) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.productService.create(new ProductRequest("후라이드", price)));
    }

    @Test
    @DisplayName("상품을 모두 조회한다.")
    void list() {
        ProductResponse product1 = this.productService.create(new ProductRequest("후라이드", BigDecimal.valueOf(16000.00)));
        ProductResponse product2 = this.productService.create(new ProductRequest("양념치킨", BigDecimal.valueOf(16000.00)));

        List<ProductResponse> products = this.productService.list();

        assertThat(products).containsAll(Arrays.asList(product1, product2));
    }

}
