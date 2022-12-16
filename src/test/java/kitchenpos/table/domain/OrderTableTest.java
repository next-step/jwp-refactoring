package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {
    @Test
    @DisplayName("주문 테이블 객체 생성")
    void createOrderTable() {
        // when
        OrderTable actual = OrderTable.of(1, false);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(OrderTable.class)
        );
    }

    @Test
    @DisplayName("테이블 생성시 최소 인원수는 0명이상이다.")
    void createOrderTableByEmpty() {
        // when & then
        assertThatThrownBy(() -> OrderTable.of(-1, true))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("최소 인원 수는 0명 이상입니다.");
    }

    @ParameterizedTest(name = "[{index}] 테이블의 비움 여부를 확인한다")
    @ValueSource(booleans = {true, false})
    void orderTableIsEmpty(boolean expect) {
        // given
        OrderTable actual = OrderTable.of(0, expect);

        // when & then
        assertThat(actual.isEmpty()).isEqualTo(expect);
    }

    @Test
    @DisplayName("단체 테이블이면 예외 발생")
    void isTableGroupThrowException() {
        // given
        OrderTable actual = OrderTable.of(null, 1L, 3, false);

        // when & then
        assertThatThrownBy(actual::validateGrouped)
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }

    @Test
    @DisplayName("테이블을 비운다")
    void changeEmpty() {
        // given
        OrderTable actual = OrderTable.of(null, 1L, 3, false);

        // when
        actual.changeEmpty();

        // then
        assertAll(
                () -> assertThat(actual.tableGroupId()).isNull(),
                () -> assertThat(actual.numberOfGuests()).isZero(),
                () -> assertTrue(actual.isEmpty())
        );
    }

    @Test
    @DisplayName("손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable actual = OrderTable.of(3, false);

        // when
        actual.changeNumberOfGuests(4);

        // then
        assertAll(
                () -> assertThat(actual.numberOfGuests()).isEqualTo(4),
                () -> assertFalse(actual.isEmpty())
        );
    }

    @Test
    @DisplayName("단체 테이블로 변경한다")
    void updateTableGroup() {
        // given
        OrderTable actual = OrderTable.of(null, null, 3, false);

        // when
        actual.updateTableGroup(100L);

        // then
        assertAll(
                () -> assertThat(actual.tableGroupId()).isEqualTo(100L),
                () -> assertFalse(actual.isEmpty())
        );
    }

    @Test
    @DisplayName("단체 그룹을 해제한다.")
    void ungroup() {
        // given
        OrderTable actual = OrderTable.of(null, 100L, 3, false);

        // when
        actual.ungroup();

        // then
        assertAll(
                () -> assertThat(actual.tableGroupId()).isNull(),
                () -> assertFalse(actual.isEmpty())
        );
    }
}
