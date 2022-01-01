package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void 상품_등록() {
        // given
        String name = "떡볶이";
        BigDecimal price = BigDecimal.valueOf(16000);
        ProductRequest request = new ProductRequest(name, price);

        // when
        ProductResponse actual = productService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
    }

    @DisplayName("상품의 가격이 0 원 이상이 아니면 등록할 수 없다.")
    @Test
    void 상품_생성_예외_음수_가격() {
        // given
        String name = "떡볶이";
        BigDecimal negativePrice = BigDecimal.valueOf(-16000);
        ProductRequest request = new ProductRequest(name, negativePrice);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> productService.create(request)
        );
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void 상품_목록_조회() {
        // given
        int savedProductSize = 6;

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual.size()).isEqualTo(savedProductSize);
        assertThat(actual.get(0).getId()).isNotNull();
        assertThat(actual.get(0).getName()).isNotNull();
        assertThat(actual.get(0).getPrice()).isNotNull();
    }
}
