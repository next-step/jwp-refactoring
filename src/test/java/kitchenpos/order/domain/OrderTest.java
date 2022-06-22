package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 Domain 단위 테스트")
class OrderTest {
    
    @DisplayName("주문 상태 일치 여부를 확인한다.")
    @Test
    void isTargetOrderStatus(){
        //given
        Order order = new Order(1L, OrderStatus.MEAL, LocalDateTime.now());

        //when
        boolean isTargetOrderStatus1 = order.isTargetOrderStatus(Arrays.asList(OrderStatus.COOKING));
        boolean isTargetOrderStatus2 = order.isTargetOrderStatus(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        boolean isTargetOrderStatus3 = order.isTargetOrderStatus(Arrays.asList(OrderStatus.MEAL));

        //then
        assertThat(isTargetOrderStatus1).isFalse();
        assertThat(isTargetOrderStatus2).isTrue();
        assertThat(isTargetOrderStatus3).isTrue();

    }

}
