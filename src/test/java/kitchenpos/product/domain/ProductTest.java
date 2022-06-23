package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProductTest {
    @DisplayName("상품을 생성한다.")
    @ParameterizedTest(name = "상품 {0}의 가격은 {1}원입니다.")
    @CsvSource(value = {"허니콤보:19000", "레드콤보:18000"}, delimiter = ':')
    void create(String name, long price) {
        Product product = Product.of(name, BigDecimal.valueOf(price));

        assertThat(product).isNotNull();
    }

    @DisplayName("상품 생성에 실패한다.")
    @ParameterizedTest(name = "상품 {0}의 가격은 {1}원입니다.")
    @CsvSource(value = {"허니콤보:-19000", "레드콤보:-18000"}, delimiter = ':')
    void createFail(String name, long price) {
        assertThatThrownBy(() -> Product.of(name, BigDecimal.valueOf(price))).isInstanceOf(IllegalArgumentException.class);
    }
}
