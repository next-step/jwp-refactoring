package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.table.domain.TableGroupFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderTable 클래스 테스트")
public class OrderTableTest {

    @DisplayName("빈 테이블 여부 값을 갱신한다")
    @Test
    void 빈_테이블_여부_갱신_테스트() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 5, false);
        Order 주문 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);

        // when
        테이블1.updateEmpty(주문, true);

        // then
        assertThat(테이블1.isEmpty()).isTrue();
    }

    @DisplayName("완료되지 않은 주문이 있는 테이블의 빈 테이블 여부 값을 갱신한다")
    @Test
    void 미완료_주문이_있는_테이블의_빈_테이블_여부_갱신_테스트() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 5, false);
        Order 주문 = 주문(1L, OrderStatus.COOKING.name(), 테이블1);

        // when & then
        assertThatThrownBy(
                () -> 테이블1.updateEmpty(주문, true)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록된 테이블의 빈 테이블 여부 값을 갱신한다")
    @Test
    void 그룹_등록된_테이블의_빈_테이블_여부_갱신_테스트() {
        // given
        TableGroup 테이블그룹 = 테이블그룹(1L);
        OrderTable 테이블1 = new OrderTable(테이블그룹, 5, false);
        Order 주문 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);

        // when & then
        assertThatThrownBy(
                () -> 테이블1.updateEmpty(주문, true)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 갱신한다")
    @Test
    void 손님_수_갱신_테스트() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 3, false);
        Order 주문 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);

        // when
        테이블1.updateNumberOfGuests(7);

        // then
        assertThat(테이블1.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("유효하지 않은 손님 수로 값을 갱신한다")
    @Test
    void 유효하지_않은_손님_수_갱신_테스트() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 5, false);

        // when & then
        assertThatThrownBy(
                () -> 테이블1.updateNumberOfGuests(-2)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 갱신한다")
    @Test
    void 빈_테이블_손님_수_갱신_테스트() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 0, true);

        // when & then
        assertThatThrownBy(
                () -> 테이블1.updateNumberOfGuests(2)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 등록한다")
    @Test
    void 테이블_그룹_테스트() {
        // given
        TableGroup 테이블그룹 = new TableGroup();
        OrderTable 테이블1 = new OrderTable(null, 0, true);

        // when
        테이블1.group(테이블그룹);

        // then
        assertAll(
                () -> assertThat(테이블1.isEmpty()).isFalse(),
                () -> assertThat(테이블1.getTableGroup()).isEqualTo(테이블그룹)
        );
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void 테이블_그룹_해제_테스트() {
        // given
        TableGroup 테이블그룹 = new TableGroup();
        OrderTable 테이블1 = new OrderTable(테이블그룹, 5, false);
        Order 주문 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);

        // when
        테이블1.unGroup(주문);

        // then
        assertAll(
                () -> assertThat(테이블1.isEmpty()).isTrue(),
                () -> assertThat(테이블1.getTableGroup()).isNull()
        );
    }

    @DisplayName("주문이 완료되지 않은 테이블 그룹을 등록한다")
    @Test
    void 주문_미완료_테이블_그룹_해제_테스트() {
        // given
        TableGroup 테이블그룹 = new TableGroup();
        OrderTable 테이블1 = new OrderTable(테이블그룹, 5, false);
        Order 주문 = 주문(1L, OrderStatus.MEAL.name(), 테이블1);

        // when & then
        assertThatThrownBy(
                () -> 테이블1.unGroup(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
