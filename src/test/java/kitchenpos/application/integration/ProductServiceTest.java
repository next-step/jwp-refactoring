package kitchenpos.application.integration;

import kitchenpos.application.ProductService;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 등록")
    @Test
    public void 상품_등록_확인() throws Exception {
        //given
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(18_000));

        //when
        ProductResponse productResponse = productService.create(productRequest);

        //then
        assertThat(productResponse.getId()).isNotNull();
    }

    @DisplayName("상품 목록 조회")
    @Test
    public void 상품_목록_조회() throws Exception {
        //given
        productService.create(new ProductRequest("후라이드1", BigDecimal.valueOf(11_000)));
        productService.create(new ProductRequest("후라이드2", BigDecimal.valueOf(12_000)));
        productService.create(new ProductRequest("후라이드3", BigDecimal.valueOf(13_000)));

        //when
        List<ProductResponse> productResponses = productService.list();

        //then
        assertThat(productResponses.size()).isEqualTo(3);
    }
}
