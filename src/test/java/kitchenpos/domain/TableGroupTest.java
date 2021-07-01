package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    @Test
    @DisplayName("모든 테이블의 모든 주문이 안끝났으면 단체지정이 해제가 불가능하므로 IllegalStateException이 발생한다 ")
    void 모든_테이블의_모든_주문이_안끝났으면_단체지정이_해제가_불가능하므로_IllegalStateException이_발생한다() {
        List<Order> orders1 = Arrays.asList(
                new Order(null, null, OrderStatus.COOKING, null, null),
                new Order(null, null, OrderStatus.COMPLETION, null, null)
        );
        List<Order> orders2 = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COMPLETION, null, null)
        );

        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(null, null, orders1, null, 1, false),
                        new OrderTable(null, null, orders2, null, 1, false)
                )
        );

        TableGroup tableGroup = new TableGroup(null, null, orderTables);

        assertThatIllegalStateException().isThrownBy(() -> tableGroup.ungroup());
    }


    @Test
    @DisplayName("모든 테이블의 모든 주문이 끝났으면 단체지정이 해제가 가능하다")
    void 모든_테이블의_모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        // given
        List<Order> orders1 = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COMPLETION, null, null)
        );
        List<Order> orders2 = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COMPLETION, null, null)
        );

        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(null, null, orders1, null, 1, false),
                        new OrderTable(null, null, orders2, null, 1, false)
                )
        );

        TableGroup tableGroup = new TableGroup(null, null, orderTables);

        // when
        tableGroup.ungroup();

        // then
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }

    }
}