package kitchenpos.tobe.orders.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.tobe.fixture.OrderTableFixture;
import kitchenpos.tobe.fixture.TableGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @DisplayName("주문 테이블 단체 지정을 생성할 수 있다.")
    @Test
    void create() {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> TableGroupFixture.of(1L);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("주문 테이블을 단체 지정 할 수 있다.")
    @Test
    void group() {
        // given
        final TableGroup group = TableGroupFixture.of(1L);

        final OrderTable table1 = OrderTableFixture.of(1L);
        final OrderTable table2 = OrderTableFixture.of(2L);
        final OrderTables tables = OrderTableFixture.ofList(table1, table2);

        // when
        group.group(tables);

        // then
        assertAll(
            () -> assertThat(table1.getTableGroupId()).isEqualTo(group.getId()),
            () -> assertThat(table2.getTableGroupId()).isEqualTo(group.getId()),
            () -> assertThat(group.getOrderTables().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 테이블을 단체 해제 할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup group = TableGroupFixture.of(1L);

        final OrderTable table1 = OrderTableFixture.of(1L);
        final OrderTable table2 = OrderTableFixture.of(2L);
        final OrderTables tables = OrderTableFixture.ofList(table1, table2);

        group.group(tables);

        // when
        group.ungroup(new FakeTableGroupValidator());

        // then
        assertAll(
            () -> assertThat(table1.getTableGroupId()).isEqualTo(0L),
            () -> assertThat(table2.getTableGroupId()).isEqualTo(0L),
            () -> assertThat(group.getOrderTables().size()).isEqualTo(0)
        );
    }
}
