package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.domain.table.OrderTable;

class OrderTablesTest {

    private TableGroup 단체_테이블그룹;

    @BeforeEach
    void setUp() {
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);
    }

    @DisplayName("OrderTables 는 OrderTable 리스트로 생성한다.")
    @Test
    void create1() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.from(1L));
        orderTables.add(OrderTable.from(2L));

        // when & then
        assertThatNoException().isThrownBy(() -> OrderTables.from(orderTables));
    }

    @DisplayName("OrderTables 생성 시, OrderTable 가 1개만 존재하면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        List<OrderTable> orderTables = Arrays.asList(OrderTable.from(1L));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderTables.from(orderTables))
                                            .withMessageContaining("최소 2개 이상의 OrderTable 이 존재해야합니다.");
    }

    @DisplayName("OrderTables 생성 시, 비어있지 않은 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.from(0));
        orderTables.add(OrderTable.from(100));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderTables.from(orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }

    @DisplayName("OrderTables 생성 시, TableGroup 에 속해있는 OrderTable 가 있으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        OrderTable 단체_테이블 = OrderTable.from(1L);
        OrderTable 개인_테이블 = OrderTable.from(2L);
        개인_테이블.alignTableGroup(단체_테이블그룹);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(단체_테이블);
        orderTables.add(개인_테이블);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderTables.from(orderTables))
                                            .withMessageContaining("OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.");
    }
}