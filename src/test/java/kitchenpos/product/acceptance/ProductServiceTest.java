package kitchenpos.product.acceptance;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("상품 통합테스트")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 관리")
    public void productManage() {
        // given
        // 상품 생성
        ProductRequest 닭껍질튀김 = new ProductRequest("닭껍질튀김", BigDecimal.valueOf(3000));
        // when
        // 상품을 등록한다.
        ProductResponse savedProduct = productService.create(닭껍질튀김);
        // then
        // 상품이 정상적으로 등록된다.
        assertThat(savedProduct).isEqualTo(new ProductResponse(savedProduct.getId(), "닭껍질튀김", BigDecimal.valueOf(3000)));
        // when
        // 상품 리스트를 조회한다.
        List<ProductResponse> savedProducts = productService.list();
        // then
        // 상품 리스트가 정상적으로 조회된다.
        assertThat(savedProducts).hasSize(7);
        assertThat(savedProducts).contains(savedProduct);
    }
}
