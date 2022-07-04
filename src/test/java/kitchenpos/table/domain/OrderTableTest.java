package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.table.domain.OrderTable.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build();
    }

    @Test
    @DisplayName("주문 테이블 객체가 같은지 검증")
    void verifyEqualsOrderTable() {
        assertThat(orderTable).isEqualTo(new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build());
    }

    @Test
    @DisplayName("테이블 그룹이 존재하면 예외 발생")
    void existTableGroupThenException() {
        final OrderTable existTableGroup = new Builder()
                .setTableGroupId(1L)
                .build();
        assertThatIllegalArgumentException()
                .isThrownBy(existTableGroup::validateExistGroupingTable);
    }

    @Test
    @DisplayName("그룹 하려는 테이블이 비어있지 않으면 예외 발생")
    void notEmptyOrderTable() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.validateGrouping());
    }

    @Test
    @DisplayName("테이블를 빈 테이블로 변경한다")
    void changeEmptyTable() {
        orderTable.changeEmpty();

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블에 있는 손님의 수를 변경한다")
    void changeGuestNumber() {
        orderTable.changeGuestNumber(3);

        assertThat(orderTable.guestNumber()).isEqualTo(GuestNumber.of(3));
    }

    @Test
    @DisplayName("그룹화된 테이블을 해제한다")
    void ungroupOrderTable() {
        final OrderTable existTableGroup = new Builder()
                .setTableGroupId(1L)
                .build();

        existTableGroup.ungroupTable();
        assertThat(existTableGroup.tableGroupId()).isNull();
    }
}
