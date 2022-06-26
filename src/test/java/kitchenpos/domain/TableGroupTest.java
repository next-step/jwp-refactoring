package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {

    OrderTable 빈_테이블1;
    OrderTable 빈_테이블2;
    OrderTable 테이블;
    OrderTable 단체_지정_테이블1;
    OrderTable 단체_지정_테이블2;
    TableGroup 기존_테이블_그룹;

    @BeforeEach
    void init() {
        // given
        빈_테이블1 = new OrderTable(null, 0, true);
        빈_테이블2 = new OrderTable(null, 0, true);
        테이블 = new OrderTable(null, 0, false);
        단체_지정_테이블1 = new OrderTable(null, 0, true);
        단체_지정_테이블2 = new OrderTable(null, 0, true);
        기존_테이블_그룹 = new TableGroup(Arrays.asList(단체_지정_테이블1, 단체_지정_테이블2));
    }

    @DisplayName("주문 테이블 단체 지정에 성공한다.")
    @Test
    void 단체_지정() {
        // when
        TableGroup 테이블_그룹 = new TableGroup(Arrays.asList(빈_테이블1, 빈_테이블2));

        // then
        assertThat(테이블_그룹).isNotNull();
        assertThat(테이블_그룹.getOrderTables()).containsExactly(빈_테이블1, 빈_테이블2);
    }

    @DisplayName("빈 테이블이 아닌 테이블이 있으면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_빈_테이블_아님() {
        // when, then
        System.out.println();
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(테이블, 빈_테이블1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블들만 단체 지정할 수 있습니다.");
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_테이블_하나() {
        // when, then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(빈_테이블1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 수가 2개 이상이어야 단체 지정할 수 있습니다");
    }

    @DisplayName("이미 단체 지정된 테이블이 있으면 단체 지정에 실패한다.")
    @Test
    void 단체_지정_예외_이미_단체_지정() {
        // when, then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(단체_지정_테이블1, 단체_지정_테이블2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 단체 지정된 테이블이 있어서 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 테이블 단체 지정 해제에 성공한다.")
    @Test
    void 단체_지정_해제() {
        // given
        assertThat(단체_지정_테이블1.getTableGroup()).isNotNull();
        assertThat(단체_지정_테이블2.getTableGroup()).isNotNull();

        // when
        기존_테이블_그룹.ungroup();

        // then
        assertThat(단체_지정_테이블1.getTableGroup()).isNull();
        assertThat(단체_지정_테이블2.getTableGroup()).isNull();
    }
}
