package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @DisplayName("주문 테이블 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        NumberOfGuests numberOfGuests = NumberOfGuests.of(4);
        Empty empty = Empty.of(false);

        // when
        OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

        // then
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests)
                , () -> assertThat(orderTable.getEmpty()).isEqualTo(empty)
        );
    }

    @DisplayName("이용 여부 수정 성공 테스트")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));
        Empty empty = Empty.of(true);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertThat(orderTable.getEmpty()).isEqualTo(empty);
    }

    @DisplayName("이용 여부 수정 실패 테스트 - 이미 속해있는 테이블 그룹 있음")
    @Test
    void changeEmpty_failure() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));
        orderTable.setTableGroup(TableGroup.of(LocalDateTime.now()));
        Empty empty = Empty.of(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeEmpty(empty));
    }

    @DisplayName("손님 수 수정 성공 테스트")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));
        NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("손님 수 수정 실패 테스트 - 주문 테이블이 비어 있음")
    @Test
    void changeNumberOfGuests_failure() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(true));
        NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests));
    }
}
