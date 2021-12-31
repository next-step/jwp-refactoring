package kitchenpos.table.domain;

import kitchenpos.exception.IsEmptyTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static kitchenpos.table.domain.TableGroupTest.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {
    public static final OrderTable 빈자리 = OrderTable.ofEmptyTable();
    public static final OrderTable 이인석 = new OrderTable(1L, 1L, 2, false);

    private OrderTable 임시자리;

    @BeforeEach
    void setUp() {
        // given
        임시자리 = OrderTable.ofEmptyTable();
    }

    @Test
    @DisplayName("주문 테이블 생성")
    public void create() {
        // given
        // when
        // then
        assertThat(임시자리).isEqualTo(OrderTable.ofEmptyTable());
    }

    @Test
    @DisplayName("테이블 그룹 부여")
    public void assignTableGroupTest() {
        // given
        // when
        임시자리.initTableGroup(테이블그룹.getId());
        // then
        assertThat(임시자리.getTableGroupId()).isEqualTo(테이블그룹.getId());
    }

    @Test
    @DisplayName("테이블 인원 변경")
    public void changeNumberOfGuestsTest() {
        // given
        OrderTable 예약석 = new OrderTable(2, false);
        // when
        예약석.changeNumberOfGuests(new NumberOfGuests(1));
        // then
        assertThat(예약석.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    @DisplayName("빈 테이블의 인원은 변경 할 수 없습니다.")
    public void isEmptyTableExceptionTest() {
        // given
        // when
        // then
        assertThatThrownBy(() -> 임시자리.changeNumberOfGuests(new NumberOfGuests(1)))
                .isInstanceOf(IsEmptyTableException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경")
    public void changeEmptyTest() {
        // given
        // when
        임시자리.changeEmpty(false);
        // then
        assertThat(임시자리.isEmpty()).isFalse();
    }

    private static Stream<Arguments> isNotNullTableGroupTestParam() {
        return Stream.of(
                Arguments.of(true, 1L),
                Arguments.of(false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("isNotNullTableGroupTestParam")
    @DisplayName("주문 테이블의 테이블 그룹(null):false, 테이블 그룹(not Null):true")
    public void isNotNullTableGroupTest(boolean status, Long tableGroup) {
        // given
        OrderTable orderTable = new OrderTable(tableGroup, 0, false);
        // when
        boolean actual = orderTable.isNotNullTableGroup();
        // then
        assertThat(actual).isEqualTo(status);
    }

}
