package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.ServiceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

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
    @MethodSource("providerCreateFailCase")
    void createFail(BigDecimal price, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
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

    private static Stream<Arguments> providerCreateFailCase() {
        return Stream.of(
            Arguments.of(null, NullPointerException.class),
            Arguments.of(BigDecimal.valueOf(-1), IllegalArgumentException.class)
        );
    }

}
