package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블들")
class OrderTablesTest {
    @DisplayName("주문 테이블이 비어 있는지 확인할 수 있다.")
    @Test
    void 주문_테이블_비어있는지_확인() {
        OrderTables 주문_테이블들 = new OrderTables();
        assertThat(주문_테이블들.isEmpty()).isTrue();
    }

    @DisplayName("특정 숫자보다 같거나 많은 수의 테이블을 가졌는지 확인할 수 있다.")
    @Test
    void 테이블_개수_비교() {
        OrderTable 주문_테이블1 = OrderTable.of(2, false);
        OrderTable 주문_테이블2 = OrderTable.of(3, false);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));
        assertThat(주문_테이블들.hasBiggerOrEqualSizeThan(2)).isTrue();
    }

    @DisplayName("주문 테이블 중 빈 테이블이 존재하는지 확인할 수 있다.")
    @Test
    void 빈_테이블_가졌는지_확인() {
        OrderTable 주문_테이블1 = OrderTable.of(2, false);
        OrderTable 주문_테이블2 = OrderTable.of(3, true);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));
        assertThat(주문_테이블들.hasEmptyOrderTable()).isTrue();
    }

    @DisplayName("단체 지정된 테이블이 있는지 확인할 수 있다.")
    @Test
    void 단체_지정된_테이블이_있는지_확인() {
        OrderTable 주문_테이블1 = new OrderTable(1L, 1L, 3, false);
        OrderTable 주문_테이블2 = OrderTable.of(3, true);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));
        assertThat(주문_테이블들.hasGroupedTable()).isTrue();
    }

    @DisplayName("단체 지정할 수 있다.")
    @Test
    void 단체_지정() {
        OrderTable 주문_테이블1 = OrderTable.of(5, false);
        OrderTable 주문_테이블2 = OrderTable.of(3, false);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        TableGroup 단체_지정 = TableGroup.of(1L, OrderTables.create());
        주문_테이블들.group(단체_지정);

        assertThat(주문_테이블들.value()).allSatisfy(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isEqualTo(1L));
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void 단체_지정_삭제() {
        OrderTable 주문_테이블1 = new OrderTable(1L, 1L, 3, false);
        OrderTable 주문_테이블2 = new OrderTable(1L, 1L, 3, false);

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        주문_테이블들.ungroup();

        assertThat(주문_테이블들.value()).allSatisfy(주문_테이블 -> assertThat(주문_테이블.getTableGroupId()).isNull());
    }
}
