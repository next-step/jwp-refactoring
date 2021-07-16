package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.table.domain.OrderTable;

@DisplayName("주문의 정책 검증")
class OrderValidatorTest {
    private OrderValidator orderValidator = new OrderValidator();

    @TestFactory
    @DisplayName("주문 관련 검증")
    List<DynamicTest> validate() {
        return Arrays.asList(
                dynamicTest("메뉴가 조회되지 않았을 경우 예외처리", () -> assertThatThrownBy(() -> orderValidator.validateExistsMenu(Optional.empty()))
                        .isInstanceOf(RuntimeException.class)),
                dynamicTest("테이블이 비어있지 않을 경우 예외처리", () -> {
                    OrderTable orderTable = new OrderTable(3, false);
                    assertThatThrownBy(() -> orderValidator.validateNotEmptyOrderTableExists(Optional.of(orderTable)))
                            .isInstanceOf(RuntimeException.class);
                })
        );
    }
}
