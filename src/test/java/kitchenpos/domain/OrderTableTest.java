package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableTest {

    @Test
    @DisplayName("모든 주문이 끝났으면 단체지정이 해제가 가능하다 ")
    void 모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        List<Order> orders = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );
        OrderTable orderTable = new OrderTable(null, null, orders, null, 1, false);

        assertThat(orderTable.isUnGroupable()).isTrue();
    }


    @Test
    @DisplayName("모든 주문이 안끝났으면 단체지정이 해제가 불가능하다 ")
    void 모든_주문이_안끝났으면_단체지정이_해제가_불가능하다() {
        List<Order> orders = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COOKING.name(), null, null)
        );
        OrderTable orderTable = new OrderTable(null, null, orders, null, 1, false);

        assertThat(orderTable.isUnGroupable()).isFalse();
    }
}