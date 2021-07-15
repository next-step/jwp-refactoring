package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @TestFactory
    @DisplayName("수정할 수 없는 주문 확인")
    List<DynamicTest> cant_modify_order() {
        return Arrays.asList(
                dynamicTest("주문 목록 중 수정불가 항목이 포함되었을 경우", () -> {
                    // given
                    given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

                    // then
                    assertThatThrownBy(() -> orderValidator.validateExistsOrdersStatusIsCookingOrMeal(Arrays.asList(1L)))
                            .isInstanceOf(OrderAlreadyExistsException.class)
                            .hasMessage("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다.");
                }),
                dynamicTest("입력 주문이 수정불가 상태일 경우", () -> {
                    // given
                    given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

                    // then
                    assertThatThrownBy(() -> orderValidator.validateExistsOrderStatusIsCookingANdMeal(1L))
                            .isInstanceOf(OrderAlreadyExistsException.class)
                            .hasMessage("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다. 입력 ID : 1");
                })
        );
    }

}
