package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품 생성 테스트")
    void createTest(){
        // given

        // when
        Product product = new Product("상품1", new BigDecimal(1000));

        // then
        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("가격 정보 없이 상품을 생성할 수 없다.")
    void createFailTest1(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Product("상품1", null)
        );
    }

    @Test
    @DisplayName("가격이 0보다 작으면 상품을 생성할 수 없다.")
    void createFailTest2(){
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Product("상품1", new BigDecimal(-100))
        );
    }
}
