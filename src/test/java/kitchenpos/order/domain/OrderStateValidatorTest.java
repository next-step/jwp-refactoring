package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class OrderStateValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderStateValidator orderStateValidator;


    @Test
    @DisplayName("주문 테이블중 조리중인 경우에 단체석을 개인 주문테이블로 변경할 수 없다.")
    void isNotAbleUngroup() {
        //given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);


        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderStateValidator.validateUnGroupTableChange(Arrays.asList(1L, 2L))
        );
    }


    @Test
    @DisplayName("주문 상태가 조리 일경우 변경할 수 없다.")
    void notChangeTableStatusIsMeal() {
        //주문상태가 조리중 경우
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderStateValidator.validateChangeEmptyTable(1L));
    }

}
