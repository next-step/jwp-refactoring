package kitchenpos.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableTestFixture.그룹_없는_주문테이블_생성;
import static kitchenpos.fixture.OrderTableTestFixture.주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupTest {

    @DisplayName("테이블 그룹 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        OrderTable 주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 10, true));
        OrderTable 주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 20, true));

        // when
        TableGroup 단체1 = TableGroup.of();

        // then
        assertAll(
                () -> assertThat(단체1).isNotNull()
        );
    }
}
