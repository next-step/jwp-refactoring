package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductServiceTest extends IntegrationServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    void create() {
        // given
        final ProductRequest product = makeProductRequest("후라이드", new BigDecimal(16000));

        // when
        final ProductResponse savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("후라이드");
    }

    @DisplayName("가격이 null이거나, 음수일 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    void createByInvalidPrice(final BigDecimal price) {
        // given
        final ProductRequest product = makeProductRequest("후라이드", price);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    private static Stream<BigDecimal> provideInvalidPrice() {
        return Stream.of(null, new BigDecimal(-1));
    }

    @Test
    void list() {
        // given
        final ProductRequest product = makeProductRequest("후라이드", new BigDecimal(16000));
        productService.create(product);

        // when
        final List<ProductResponse> products = productService.list();

        // then
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("후라이드");
    }

    public static ProductRequest makeProductRequest(final String name, final BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
