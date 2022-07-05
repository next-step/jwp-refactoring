package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.table.TableGenerator.*;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTableTest {

    @DisplayName("단체 지정에 포함된 주문 테이블의 빈 자리 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByBelongTableGroupTest() {
        // given
        OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));
        주문_테이블.joinGroup(테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블))));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_테이블.updateEmpty(false, OrderStatus.COMPLETION));
    }

    @DisplayName("주문 테이블의 주문이 종료 상태가 아닌 상태인 주문 테이블의 빈 자리 여부 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByNotCompletionOrderStatusTest() {
        // given
        OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_테이블.updateEmpty(false, OrderStatus.COOKING));
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_테이블.updateEmpty(false, OrderStatus.MEAL));
    }

    @DisplayName("단체 지정에 포함되지 않고 완료 상태의 주문 테이블의 빈 자리 여부를 변경하면 정상 변경되어야 한다")
    @Test
    void changeEmptyTest() {
        // given
        OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));

        // when
        주문_테이블.updateEmpty(false, OrderStatus.COMPLETION);

        // then
        assertThat(주문_테이블.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블이 빈 테이블 상태에서 손님 수를 변경하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByEmptyTest() {
        // given
        OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_테이블.updateNumberOfGuests(손님_수_생성(5)));
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닐 때 손님 수를 변경하면 정상 변경되어야 한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));
        주문_테이블.updateEmpty(false, OrderStatus.COMPLETION);

        // when
        주문_테이블.updateNumberOfGuests(손님_수_생성(5));

        // then
        assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(손님_수_생성(5));
    }
}
