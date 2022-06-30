package kitchenpos.table.unit;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.table.unit.OrderTableTest.테이블_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @DisplayName("주문 테이블 단체 지정에 성공한다.")
    @Test
    void 단체_지정() {
        // given
        OrderTable 빈_테이블1 = 테이블_생성되어_있음(0, true);
        OrderTable 빈_테이블2 = 테이블_생성되어_있음(0, true);

        // when
        TableGroup 테이블_그룹 = new TableGroup(Arrays.asList(빈_테이블1, 빈_테이블2));

        // then
        assertThat(테이블_그룹).isNotNull();
        assertThat(테이블_그룹.getOrderTables()).containsExactly(빈_테이블1, 빈_테이블2);
    }

    @DisplayName("빈 테이블이 아닌 테이블이 있으면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_빈_테이블_아님() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderTable 빈_테이블 = 테이블_생성되어_있음(0, true);

        // when, then
        System.out.println();
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(테이블, 빈_테이블)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블들만 단체 지정할 수 있습니다.");
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_테이블_하나() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, true);

        // when, then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(테이블)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 수가 2개 이상이어야 단체 지정할 수 있습니다");
    }

    @DisplayName("이미 단체 지정된 테이블이 있으면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_이미_단체_지정() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, true);
        OrderTable 단체_지정_테이블1 = 테이블_생성되어_있음(0, true);
        OrderTable 단체_지정_테이블2 = 테이블_생성되어_있음(0, true);
        테이블_단체_지정되어_있음(단체_지정_테이블1, 단체_지정_테이블2);

        // when, then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(테이블, 단체_지정_테이블1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 단체 지정된 테이블이 있어서 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 테이블 단체 지정 해제에 성공한다.")
    @Test
    void 단체_지정_해제() {
        // given
        OrderTable 단체_지정_테이블1 = 테이블_생성되어_있음(0, true);
        OrderTable 단체_지정_테이블2 = 테이블_생성되어_있음(0, true);
        TableGroup 테이블_그룹 = 테이블_단체_지정되어_있음(단체_지정_테이블1, 단체_지정_테이블2);
        assertThat(단체_지정_테이블1.getTableGroup()).isNotNull();
        assertThat(단체_지정_테이블2.getTableGroup()).isNotNull();

        // when
        테이블_그룹.ungroup();

        // then
        assertThat(단체_지정_테이블1.getTableGroup()).isNull();
        assertThat(단체_지정_테이블2.getTableGroup()).isNull();
    }

    static TableGroup 테이블_단체_지정되어_있음(OrderTable... orderTables) {
        return new TableGroup(Arrays.asList(orderTables));
    }
}
