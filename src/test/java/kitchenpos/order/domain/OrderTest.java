package kitchenpos.order.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import kitchenpos.order.OrderFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문항목이 없는경우 에러 발생")
    void noOrderLineItemException(){
        assertThatThrownBy(() -> new Order(1L, 일번테이블, null, null,
            Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    @DisplayName("주문이 계산 완료된 이후 상태 변경시 에러 발생")
    void changeOrderStatusWhenItCompleted(){
        Order 주문 = new Order(1L, 일번테이블, COMPLETION.name(), null,
            Collections.singletonList(주문항목));
        assertThatThrownBy(() -> 주문.setOrderStatus(OrderStatus.MEAL.name()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("계산이 완료 되었습니다. 주문 상태 변경이 불가능 합니다.");
    }

    @Test
    @DisplayName("주문 테이블정보가 없으면 에러 발생")
    void noOrderTableException(){
        assertThatThrownBy(() -> new Order(1L, null, null, null, Collections.singletonList(주문항목)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블 정보가 없습니다.");
    }

}
