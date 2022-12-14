package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
@DisplayName("상품 테스트")
public
class ProductTest {

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
        final int minusPrice = -1;
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(상품("이름", BigDecimal.valueOf(minusPrice))));
    }

    /**
     * source:
     * INSERT INTO product (id, name, price) VALUES (1, '후라이드', 16000);
     * INSERT INTO product (id, name, price) VALUES (2, '양념치킨', 16000);
     * INSERT INTO product (id, name, price) VALUES (3, '반반치킨', 16000);
     * INSERT INTO product (id, name, price) VALUES (4, '통구이', 16000);
     * INSERT INTO product (id, name, price) VALUES (5, '간장치킨', 17000);
     * INSERT INTO product (id, name, price) VALUES (6, '순살치킨', 17000);
     */
    @DisplayName("목록 조회 성공")
    @Test
    void list_product_success() {
        assertThat(productService.list()).hasSize(6);
    }

    public static Product 상품(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
