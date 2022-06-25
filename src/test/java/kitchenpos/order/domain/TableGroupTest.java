package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        TableGroup tableGroup = TableGroup.from(1L, Lists.newArrayList(주문테이블));
        assertThat(tableGroup).isEqualTo(tableGroup);
    }
}
