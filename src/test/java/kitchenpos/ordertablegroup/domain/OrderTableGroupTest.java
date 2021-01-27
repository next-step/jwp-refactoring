package kitchenpos.ordertablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableGroupTest {

    @DisplayName("주문 테이블 그룹을 생성한다.")
    @Test
    void constructor() {
        // when
        OrderTableGroup orderTableGroup = new OrderTableGroup();

        // then
        assertThat(orderTableGroup).isNotNull();
    }
}
