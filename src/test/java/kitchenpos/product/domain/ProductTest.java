package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.product.dto.ProductRequest;

@DisplayName("상품 도메인 테스트")
class ProductTest {

    @TestFactory
    @DisplayName("상품 객체 생성 예외 처리")
    List<DynamicTest> price_validate() {
        return Arrays.asList(
                dynamicTest("금액이 입력되지 않은 경우 오류 발생.", () -> {
                    // then
                    assertThatThrownBy(() -> new Product("A", null))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("금액은 Null이 아닌 0 이상의 금액이어야합니다.");
                }),
                dynamicTest("금액에 음수 입력될 경우 오류 발생.", () -> {
                    // then
                    assertThatThrownBy(() -> new Product("A", BigDecimal.valueOf(-1)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("금액은 Null이 아닌 0 이상의 금액이어야합니다.");
                }),
                dynamicTest("상품 이름이 입력되지 않았을 경우 예외처리.", () -> {
                    // then
                    assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(-1)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("이름은 Null이거나 공백일 수 없습니다.");
                }),
                dynamicTest("상품 이름이 공백일 경우 예외처리.", () -> {
                    // then
                    assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(-1)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("이름은 Null이거나 공백일 수 없습니다.");
                })
        );
    }
}
