package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    @Test
    void 주문_테이블의_비어있는_여부를_수정할_수_있다() {
        // given
        boolean expectEmpty = true;
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when
        orderTable.updateEmpty(expectEmpty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectEmpty);
    }

    @Test
    void 주문_테이블의_비어있는_여부를_수정할_때_단체_지정되어_있다면_수정할_수_없다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        firstOrderTable.updateTableGroup(tableGroup.getId());

        // when & then
        assertThatThrownBy(() -> firstOrderTable.updateEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ALREADY_GROUP.message());
    }

    @Test
    void 주문_테이블의_손님_수를_수정할_수_있다() {
        // given
        NumberOfGuests expectNumberOfGuest = new NumberOfGuests(10);
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when
        orderTable.updateNumberOfGuest(expectNumberOfGuest);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuest.value());
    }

    @Test
    void 주문_테이블의_손님_수를_수정_할_때_주문테이블이_빈상태이면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), true);

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuest(new NumberOfGuests(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), true);

        // when
        firstOrderTable.ungroup();

        // then
        assertThat(firstOrderTable.getTableGroupId()).isNull();
    }
}
