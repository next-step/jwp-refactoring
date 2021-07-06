package kitchenpos.domain.table;

import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class TableGroupTest {
    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();
    }

    @Test
    @DisplayName("모든 테이블의 모든 주문이 안끝났으면 단체지정이 해제가 불가능하므로 IllegalStateException이 발생한다 ")
    void 모든_테이블의_모든_주문이_안끝났으면_단체지정이_해제가_불가능하므로_IllegalStateException이_발생한다() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(사용중인_1명_1건_결제완료_1건_식사, 사용중인_1명_2건_결제완료1));

        TableGroup tableGroup = new TableGroup(null, null, orderTables);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> tableGroup.ungroup());
    }


    @Test
    @DisplayName("모든 테이블의 모든 주문이 끝났으면 단체지정이 해제가 가능하다")
    void 모든_테이블의_모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(사용중인_1명_2건_결제완료1, 사용중인_1명_2건_결제완료2));

        TableGroup tableGroup = new TableGroup(null, null, orderTables);

        // when
        tableGroup.ungroup();

        // then
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            assertThat(orderTable.getTableGroup()).isNull();
        }

    }
}