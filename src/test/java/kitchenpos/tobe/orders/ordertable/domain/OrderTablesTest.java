package kitchenpos.tobe.orders.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.tobe.fixture.OrderTableFixture;
import kitchenpos.tobe.fixture.TableGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {

    @DisplayName("주문 테이블 일급 컬렉션을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable table1 = OrderTableFixture.of(1L);
        final OrderTable table2 = OrderTableFixture.of(2L);

        // when
        final ThrowableAssert.ThrowingCallable request = () -> OrderTableFixture.ofList(
            table1,
            table2
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("주문 테이블 묶음을 단체 지정할 수 있다.")
    @Test
    void group() {
        // given
        final OrderTable table1 = OrderTableFixture.of(1L);
        final OrderTable table2 = OrderTableFixture.of(2L);
        final OrderTables tables = OrderTableFixture.ofList(table1, table2);

        final TableGroup group = TableGroupFixture.of(1L);

        // when
        tables.group(group);

        // then
        assertAll(
            () -> assertThat(table1.getTableGroupId()).isEqualTo(group.getId()),
            () -> assertThat(table2.getTableGroupId()).isEqualTo(group.getId()),
            () -> assertThat(tables.asList().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 테이블 묶음을 단체 지정할 수 있다.")
    @Test
    void ungroup() {
        // given
        final OrderTable table1 = OrderTableFixture.of(1L);
        final OrderTable table2 = OrderTableFixture.of(2L);
        final OrderTables tables = OrderTableFixture.ofList(table1, table2);

        // when
        tables.ungroup();

        // then
        assertAll(
            () -> assertThat(table1.getTableGroupId()).isEqualTo(0L),
            () -> assertThat(table2.getTableGroupId()).isEqualTo(0L),
            () -> assertThat(tables.asList().size()).isEqualTo(0)
        );
    }
}
