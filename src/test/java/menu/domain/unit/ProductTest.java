package menu.domain.unit;

import static fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.*;

import org.junit.jupiter.api.*;

import menu.domain.*;

@DisplayName("상품 관련(단위테스트)")
class ProductTest {

    @DisplayName("상품 생성하기")
    @Test
    void createTest() {
        assertThat(Product.of(상품_후라이드치킨.getName(), 상품_후라이드치킨.getPrice())).isInstanceOf(Product.class);
    }

    @DisplayName("상품 가격이 음수이면 생성 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(() -> Product.of(상품_후라이드치킨.getName(), BigDecimal.valueOf(-1L))).isInstanceOf(IllegalArgumentException.class);
    }
}
