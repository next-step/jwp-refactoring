package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Product 클래스 테스트")
public class ProductTest {

    @DisplayName("Product를 생성한다")
    @Test
    void Product_생성() {
        Product product = new Product("알리오올리오",17000);

        assertAll(
                () -> assertThat(product.getName()).isEqualTo("알리오올리오"),
                () -> assertThat(product.getPrice().intValue()).isEqualTo(17000)
        );
    }

    @DisplayName("유효하지 않은 가격으로 Product를 생성한다")
    @Test
    void 유효하지_않은_가격의_Product_생성() {

        assertThatThrownBy(
                () -> new Product("알리오올리오", -17000)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
