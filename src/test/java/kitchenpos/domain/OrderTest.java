package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AtLeastOneOrderLineItemException;
import kitchenpos.exception.CannotChangeOrderStatusException;
import kitchenpos.exception.NotEmptyTableException;

class OrderTest {

    @DisplayName("[주문 등록] 빈 주문항목목록으로 주문을 등록할 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Order(new OrderTable(true), Collections.emptyList()))
            .isInstanceOf(AtLeastOneOrderLineItemException.class);
    }

    @DisplayName("[주문등록] 주문테이블은 비어있어야 한다")
    @Test
    void test2() {
        assertThatThrownBy(() -> new Order(new OrderTable(false), Collections.emptyList()))
            .isInstanceOf(NotEmptyTableException.class);
    }

    @DisplayName("[주문상태 변경] 주문상태가 완료인 경우 상태변경할 수 없다")
    @Test
    void test3() {
        assertThatThrownBy(() -> new Order(COMPLETION).changeStatus(COOKING))
            .isInstanceOf(CannotChangeOrderStatusException.class);
    }

}
