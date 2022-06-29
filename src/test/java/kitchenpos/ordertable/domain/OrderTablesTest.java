package kitchenpos.ordertable.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {
    OrderTable 치킨주문테이블;

    @BeforeEach
    void setUp() {
        치킨주문테이블 = createOrderTable(1L, 2, true);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        OrderTables orderTables = OrderTables.from(Lists.newArrayList(치킨주문테이블));
        assertThat(orderTables.readOnlyOrderTables()).isEqualTo(Lists.newArrayList(치킨주문테이블));
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> OrderTables.from(null))
                .withMessage("주문 테이블들이 필요합니다.");
    }
}
