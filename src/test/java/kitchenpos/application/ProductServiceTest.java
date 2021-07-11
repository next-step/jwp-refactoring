package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.presentation.dto.ProductRequest;
import kitchenpos.product.presentation.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("상품 관리")
class ProductServiceTest extends DataBaseCleanSupport {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        //when
        ProductResponse actual = productService.create(ProductRequest.of("후라이드치킨", BigDecimal.valueOf(10000)));

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("후라이드치킨");
        assertThat(actual.getPrice().longValue()).isEqualTo(10000L);
    }

    @DisplayName("상품의 가격을 지정해야한다.")
    @Test
    void createProductExceptionIfPriceIsNull() {
        //given
        ProductRequest productRequest = ProductRequest.of("후라이드치킨", null);

        //when
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void createProductExceptionIfPriceIsNegative() {
        //given
        ProductRequest productRequest = ProductRequest.of("후라이드치킨", BigDecimal.valueOf(-1000));

        //when
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("상품의 이름을 지정해야한다.")
    @Test
    void createProductExceptionIfNameIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("상품을 모두 조회한다.")
    @Test
    void list() {
        //when
        List<ProductResponse> actual = productService.list();

        //then
        assertThat(actual.size()).isNotZero();
    }
}
