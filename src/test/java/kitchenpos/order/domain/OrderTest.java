package kitchenpos.order.domain;

import kitchenpos.order.domain.order.Order;
import kitchenpos.table.domain.table.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관리")
class OrderTest {

    @Nested
    @DisplayName("주문 생성")
    class CreateOrder {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            OrderTable 한식테이블 = 한식_테이블;

            // when
            Order order = new Order(한식테이블);

            // then
            assertThat(order.getOrderTable()).isEqualTo(한식_테이블);
        }

        @Test
        @DisplayName("실패 - 주문 테이블 없음")
        public void failOrderTableEmpty() {
            // given
            OrderTable 한식테이블 = null;

            // when
            Assertions.assertThatThrownBy(() -> {
                Order order = new Order(한식테이블);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
