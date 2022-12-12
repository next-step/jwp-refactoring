package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableTestFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableTestFixture.그룹_없는_주문테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupTest {

    @Test
    void of() {
        // when
        long expectedId = 1L;
        OrderTable 주문테이블1 = 그룹_없는_주문테이블_생성(createOrderTable(null, null, 10, true));
        OrderTable 주문테이블2 = 그룹_없는_주문테이블_생성(createOrderTable(null, null, 20, true));
        TableGroup 단체1 = TableGroup.of(expectedId, Arrays.asList(주문테이블1, 주문테이블2), Arrays.asList(주문테이블1, 주문테이블2));

        // then
        assertAll(
                () -> assertThat(단체1).isNotNull(),
                () -> assertThat(단체1.getId()).isEqualTo(expectedId),
                () -> assertThat(단체1.getOrderTables()).containsExactly(주문테이블1, 주문테이블2)
        );
    }
}
