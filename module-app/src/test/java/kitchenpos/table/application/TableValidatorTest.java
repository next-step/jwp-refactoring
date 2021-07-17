package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.CannotTableChangeEmptyException;
import kitchenpos.order.domain.Order;

@DisplayName("테이블 정책 기능 검증")
class TableValidatorTest {
    private TableValidator tableValidator = new TableValidator();

    @TestFactory
    @DisplayName("수정하려는 대상의 주문상태 검증")
    List<DynamicTest> validate_orderStatus() {
        return Arrays.asList(
                dynamicTest("대상이 존재하고, 주문 상태가 COOKING, MEAL 상태일 경우 예외 발생", () -> {
                    // given
                    Order order = new Order(LocalDateTime.now(), 1L);
                    order.changeOrderStatus(OrderStatus.MEAL);

                    // then
                    assertThatThrownBy(() -> tableValidator.validateExistsOrderStatusIsCookingANdMeal(Optional.of(order)))
                            .isInstanceOf(CannotTableChangeEmptyException.class)
                            .hasMessageContaining("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다.");
                })
        );
    }
}
