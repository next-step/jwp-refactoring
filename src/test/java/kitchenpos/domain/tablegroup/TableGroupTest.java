package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.domain.table.OrderTable;

class TableGroupTest {

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    void setUp() {
        테이블1 = OrderTableFixtureFactory.createWithGuests(1L, 0, true);
        테이블2 = OrderTableFixtureFactory.createWithGuests(2L, 0, true);
    }

    @DisplayName("TableGroup 은 OrderTables 로 생성된다.")
    @Test
    void create1() {
        // given
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블2);

        // when
        TableGroup tableGroup = TableGroup.from(orderTables);

        // then
        assertThat(tableGroup.getOrderTables().getValues()).containsExactly(테이블1, 테이블2);
    }

    @DisplayName("TableGroup 생성 시, OrderTables 가 1개만 존재하면 예외가 발생한다.")
    @Test
    void create2() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> TableGroup.from(Arrays.asList(테이블1)))
                                            .withMessageContaining("최소 2개 이상의 OrderTable 이 존재해야합니다.");
    }

    @DisplayName("TableGroup 생성 시, OrderTables 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> TableGroup.from(Collections.emptyList()))
                                            .withMessageContaining("최소 2개 이상의 OrderTable 이 존재해야합니다.");
    }

    @DisplayName("TableGroup 생성 시, 비어있지 않은 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.from(0));
        orderTables.add(OrderTable.from(100));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> TableGroup.from(orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }

    @DisplayName("TableGroup 생성 시, 이미 특정 TableGroup 에 속해있는 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        OrderTable 단체_테이블 = OrderTable.from(1L);
        OrderTable 개인_테이블 = OrderTable.from(2L);
        개인_테이블.alignTableGroup(TableGroup.from(1L));

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(단체_테이블);
        orderTables.add(개인_테이블);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> TableGroup.from(orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }
}