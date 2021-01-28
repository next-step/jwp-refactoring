package kitchenpos.product;

import kitchenpos.BaseServiceTest;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static kitchenpos.utils.TestHelper.등록되어_있지_않은_product_id;

class ProductServiceTest extends BaseServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        ProductRequest productRequest = new ProductRequest(등록되어_있지_않은_product_id, "스노윙치킨", BigDecimal.valueOf(18000));

        ProductResponse result = productService.create(productRequest);

        assertThat(result.getId()).isEqualTo(productRequest.getId());
    }

    @DisplayName("상품의 가격이 설정되어 있지 않을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException1() {
        ProductRequest productRequest = new ProductRequest(등록되어_있지_않은_product_id, "스노윙치킨", null);

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("상품의 가격이 0원보다 작을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException2() {
        ProductRequest productRequest = new ProductRequest(등록되어_있지_않은_product_id, "스노윙치킨", BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }
}
