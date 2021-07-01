package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {

    @Test
    @DisplayName("모든 주문이 끝났으면 단체지정이 해제가 가능하다 ")
    void 모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        List<Order> orders = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COMPLETION, null, null)
        );
        OrderTable orderTable = new OrderTable(null, null, orders,  1, false);

        assertThat(orderTable.isUnGroupable()).isTrue();
    }


    @Test
    @DisplayName("모든 주문이 안끝났으면 단체지정이 해제가 불가능하다 ")
    void 모든_주문이_안끝났으면_단체지정이_해제가_불가능하다() {
        List<Order> orders = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COOKING, null, null)
        );
        OrderTable orderTable = new OrderTable(null, null, orders,  1, false);

        assertThat(orderTable.isUnGroupable()).isFalse();
    }

    @Test
    @DisplayName("모든 주문이 안끝났으면 단체지정이 해제가 불가능 하므로 IllegalStateException이 발생한다 ")
    void 모든_주문이_안끝났으면_단체지정이_해제가_불가능_하므로_IllegalStateException이_발생한다() {
        List<Order> orders = Arrays.asList(
                new Order(null, null, OrderStatus.COMPLETION, null, null),
                new Order(null, null, OrderStatus.COOKING, null, null)
        );
        OrderTable orderTable = new OrderTable(null, null, orders,  1, false);

        assertThatIllegalStateException().isThrownBy(() -> orderTable.ungroup());
    }

    @Test
    @DisplayName("빈 테이블일 경우 인원수를 바꾸려 하면 IllegalStateException이 발생한다")
    void 빈_테이블을_경우_인원수를_바꾸려_하면_IllegalStateException이_발생한다() {
        OrderTable orderTable = new OrderTable(null, Arrays.asList(), 0, true);

        assertThatIllegalStateException().isThrownBy(() -> orderTable.changeNumberOfGuest(new NumberOfGuest(1)));
    }


    @Test
    @DisplayName("빈 테이블이 아닐경우 인원수를 바꾸려 하면 성공한다")
    void 빈_테이블이_아닐경우_인원수를_바꾸려_하면_성공한다() {
        OrderTable orderTable = new OrderTable(null, Arrays.asList(), 0, false);

        assertDoesNotThrow(() -> orderTable.changeNumberOfGuest(new NumberOfGuest(1)));
    }

    @Test
    @DisplayName("이미 예약이 되어있으면 예약이 불가능하다")
    void 이미_예약이_되어있으면_예약이_불가능하다() {
        OrderTable orderTable = new OrderTable(null, Arrays.asList(), 1, false);

        assertThatIllegalStateException().isThrownBy(() -> orderTable.bookedBy(null));
    }

    @Test
    @DisplayName("정상적인 예약")
    void 정상적인_예약() {
        OrderTable orderTable = new OrderTable(null, Arrays.asList(), 1, true);
        TableGroup tableGroup = new TableGroup();

        orderTable.bookedBy(tableGroup);

        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
    }
}