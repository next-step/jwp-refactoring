package kitchenpos.application;

import kitchenpos.domain.product.InvalidProductPriceException;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("요청한 상품의 가격이 null이거나 0원 미만인 경우 상품을 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("createProductFailTestResource")
    void createProductFailTest(BigDecimal invalidPrice) {
        // given
        String name = "new product";
        ProductRequest productRequest = new ProductRequest(name, invalidPrice);

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(InvalidProductPriceException.class)
                .hasMessage("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
    }
    public static Stream<Arguments> createProductFailTestResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("상품을 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = { 0, 1 })
    void createProductTest(Long price) {
        // given
        String productName = "닭강정";

        ProductRequest productRequest = new ProductRequest(productName, BigDecimal.valueOf(price));

        // when
        ProductResponse created = productService.create(productRequest);

        // then
        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProductsTest() {
        // given
        String productName = "닭강정";

        ProductRequest productRequest = new ProductRequest(productName, BigDecimal.ONE);

        ProductResponse saved = productService.create(productRequest);

        // when
        List<ProductResponse> foundProducts = productService.list();
        Stream<Long> ids = foundProducts.stream()
                .map(ProductResponse::getId);

        // then
        assertThat(ids).contains(saved.getId());
    }
}