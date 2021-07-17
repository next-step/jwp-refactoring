package api.menu.application;

import api.DataBaseCleanSupport;
import api.menu.dto.ProductRequest;
import api.menu.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @DisplayName("상품을 모두 조회한다.")
    @Test
    void list() {
        //when
        List<ProductResponse> actual = productService.findProductResponses();

        //then
        assertThat(actual.size()).isNotZero();
    }
}
