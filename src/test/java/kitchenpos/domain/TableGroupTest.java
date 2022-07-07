package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTables = Arrays.asList(new OrderTable(0, true),
                new OrderTable(0, true));
    }

    @Test
    void 테이블_그룹_생성() {
        TableGroup tableGroup = new TableGroup();

        assertThat(tableGroup.getCreatedDate()).isNotNull();
    }

    @Test
    void 테이블_추가() {
        TableGroup tableGroup = new TableGroup();

        tableGroup.addOrderTables(orderTables);

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    void 한개의_테이블만_그룹에_추가하는_경우() {
        TableGroup tableGroup = new TableGroup();

        orderTables = Collections.singletonList(new OrderTable(0, true));

        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 추가하는_테이블이_빈_테이블이_아닌경우() {
        TableGroup tableGroup = new TableGroup();

        orderTables = Arrays.asList(new OrderTable(0, true),
                new OrderTable(0, false));

        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_그룹이_존재하는_경우() {
        TableGroup tableGroup = new TableGroup();

        orderTables = Arrays.asList(new OrderTable(0, true),
                new OrderTable(0, false));

        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹_해제() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(orderTables);

        tableGroup.ungroup();

        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}