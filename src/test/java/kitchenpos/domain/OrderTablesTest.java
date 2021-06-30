package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {
    private final OrderTables hasTableGroupIdOrderTables = new OrderTables(
            Arrays.asList(
                    new OrderTable(1L, 1L, 1, true),
                    new OrderTable(1L, null, 1, true)
            )
    );

    private final OrderTables notEmptyOrderTables = new OrderTables(
            Arrays.asList(
                    new OrderTable(1L, null, 1, false),
                    new OrderTable(1L, null, 1, true)
            )
    );


    @Test
    @DisplayName("하나라도 Empty가 true이면 예약이 되어있다")
    void 하나라도_Empty가_True_이면_예약이_되어있다() {
        assertThat(notEmptyOrderTables.isBookedAny()).isTrue();
    }

    @Test
    @DisplayName("하나라도 TableGroup이 지정되있으면 예약이 되어있다")
    void 하나라도_TableGroup이_지정되어있으면_예약이_되어있다() {
        assertThat(hasTableGroupIdOrderTables.isBookedAny()).isTrue();
    }

    @Test
    @DisplayName("TableGroup이 지정이 안되어있고, 빈 테이블이면 예약이 안되어있다.")
    void TableGroup이_지정이_안되어있고_빈_테이블이면_예약이_안되어있다() {
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(1L, null, 1, true),
                        new OrderTable(1L, null, 1, true)
                )
        );

        assertThat(orderTables.isBookedAny()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    @DisplayName("개수에 맞게 size를 리턴한다")
    void size(int len) {
        List<OrderTable> orderTableList = new ArrayList<>();
        for (int i = 0; i<len; i++) {
            orderTableList.add(new OrderTable(null, null, 0, false));
        }

        assertThat(new OrderTables(orderTableList).size()).isEqualTo(len);
    }

    @Test
    @DisplayName("예약이 되어있는 상태에서 예약시 IllegalStateException이 발생한다")
    void 예약이_되어있는_상태에서_예약시_IllegalStateException이_발생한다() {
        assertThatIllegalStateException().isThrownBy(() -> hasTableGroupIdOrderTables.bookedBy(null));
        assertThatIllegalStateException().isThrownBy(() -> notEmptyOrderTables.bookedBy(null));
    }


    @Test
    @DisplayName("모든 테이블의 모든 주문이 끝났으면 단체지정이 해제가 가능하다 ")
    void 모든_테이블의_모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        List<Order> orders1 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );
        List<Order> orders2 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );

        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                    new OrderTable(null, null, orders1, null, 1, false),
                    new OrderTable(null, null, orders2, null, 1, false)
                )
        );

        assertThat(orderTables.isUnGroupable()).isTrue();
    }


    @Test
    @DisplayName("모든 테이블의 모든 주문이 안끝났으면 단체지정이 해제가 불가능하다 ")
    void 모든_테이블의_모든_주문이_안끝났으면_단체지정이_해제가_불가능하다() {
        List<Order> orders1 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COOKING.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );
        List<Order> orders2 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );

        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(null, null, orders1, null, 1, false),
                        new OrderTable(null, null, orders2, null, 1, false)
                )
        );

        assertThat(orderTables.isUnGroupable()).isFalse();
    }

    @Test
    @DisplayName("모든 테이블의 모든 주문이 안끝났으면 단체지정이 해제가 불가능하므로 IllegalStateException이 발생한다 ")
    void 모든_테이블의_모든_주문이_안끝났으면_단체지정이_해제가_불가능하므로_IllegalStateException이_발생한다() {
        List<Order> orders1 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COOKING.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );
        List<Order> orders2 = Arrays.asList(
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null),
                new Order(null, null, null, OrderStatus.COMPLETION.name(), null, null)
        );

        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(null, null, orders1, null, 1, false),
                        new OrderTable(null, null, orders2, null, 1, false)
                )
        );

        assertThatIllegalStateException().isThrownBy(() -> orderTables.ungroup());
    }
}