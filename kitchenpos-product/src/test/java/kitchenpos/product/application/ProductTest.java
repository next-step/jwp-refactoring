package kitchenpos.product.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
@DisplayName("상품 테스트")
public class ProductTest {

    @Autowired
    private ProductService productService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final String name = "이름";
        final BigDecimal price = BigDecimal.valueOf(1000.0);
        //when, then:
        assertThat(productService.create(상품(name, price)).equals(상품(name, price))).isTrue();
    }

    @DisplayName("생성 실패 - 상품의 가격이 음수인 경우")
    @Test
    void create_product_IllegalArgumentException() {
        //given:
        final int minusPrice = -1;
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(상품("이름", BigDecimal.valueOf(minusPrice))));
    }

    @DisplayName("목록 조회 성공")
    @Test
    void list_product_success() {
        //given:
        productService.create(상품("상품"));
        //when, then:
        assertThat(productService.list()).isNotEmpty();
    }
}
