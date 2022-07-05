package kitchenpos.table.domain;

import kitchenpos.table.TableGenerator;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.TableGenerator.주문_테이블_목록_생성;
import static kitchenpos.table.TableGenerator.테이블_그룹_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TableGroupTest {

    private OrderTable 주문_테이블 = TableGenerator.주문_테이블_생성(손님_수_생성(10));

    @DisplayName("단체 지정 생성 시 주문 테이블 수가 1개 이하이면 예외가 발생해야 한다")
    @Test
    void createTableGroupByContainUnderOneOrderTable() {
        assertThatIllegalArgumentException().isThrownBy(() -> 테이블_그룹_생성(주문_테이블_목록_생성(Collections.singletonList(주문_테이블))));
        assertThatIllegalArgumentException().isThrownBy(() -> 테이블_그룹_생성(주문_테이블_목록_생성(Collections.emptyList())));
    }

    @DisplayName("그룹 테이블 생성 요청 시 이미 단체 지정에 속한 주문 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void createTableGroupByAlreadyBelongGroupTest() {
        // given
        OrderTable 테이블_그룹에_속해있는_주문_테이블 = TableGenerator.주문_테이블_생성(손님_수_생성(10));
        테이블_그룹에_속해있는_주문_테이블.joinGroup(new TableGroup(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 테이블_그룹에_속해있는_주문_테이블))));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 테이블_그룹에_속해있는_주문_테이블))));
    }

    @DisplayName("정상 상태의 그룹 테이블 생성 요청 시 정상 동작해야 한다")
    @Test
    void createTableGroupTest() {
        // when
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // then
        assertThat(테이블_그룹.getOrderTables().getValue().get(0)).isEqualTo(주문_테이블);
        assertThat(주문_테이블.getTableGroup()).isEqualTo(테이블_그룹);
    }

    @DisplayName("그룹 테이블 해제시 대상 주문 테이블의 그룹 테이블 정보가 없어져야 한다")
    @Test
    void ungroupTest() {
        // given
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // when
        테이블_그룹.ungroup();

        // then
        assertThat(주문_테이블.getTableGroup()).isNull();
    }
}
