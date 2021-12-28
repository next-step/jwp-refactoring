package kitchenpos.tobe.orders.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.tobe.common.domain.CustomEventPublisher;
import kitchenpos.tobe.fixture.OrderTableFixture;
import kitchenpos.tobe.fixture.TableGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable table = OrderTableFixture.of();

        // when
        final int numberOfGuests = table.getNumberOfGuests();
        final boolean isEmpty = table.isEmpty();

        // then
        assertAll(
            () -> assertThat(numberOfGuests).isEqualTo(0),
            () -> assertThat(isEmpty).isTrue()
        );
    }

    @DisplayName("주문 테이블을 손님에게 제공할 수 있다.")
    @Test
    void serve() {
        // given
        final OrderTable table = OrderTableFixture.of();

        // when
        final int expectedNumberOfGuests = 4;
        table.serve(new NumberOfGuests(expectedNumberOfGuests));

        // then
        assertAll(
            () -> assertThat(table.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
            () -> assertThat(table.isEmpty()).isFalse()
        );
    }

    @DisplayName("이미 제공된 주문 테이블을 손님에게 제공할 수 있다.")
    @Test
    void serveFailTableNotEmpty() {
        // given
        final OrderTable table = OrderTableFixture.of();
        table.serve(new NumberOfGuests(4));

        // when
        final ThrowableAssert.ThrowingCallable request = () -> table.serve(new NumberOfGuests(2));

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문 테이블을 정리할 수 있다.")
    @Test
    void clear() {
        // given
        final CustomEventPublisher<OrderTable> eventPublisher = new FakeOrderTableEventPublisher();
        final OrderTable table = OrderTableFixture.of();
        table.serve(new NumberOfGuests(4));

        // when
        table.clear(eventPublisher);

        // then
        assertAll(
            () -> assertThat(table.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(table.isEmpty()).isTrue()
        );
    }

    @DisplayName("빈 주문 테이블을 정리할 수 없다.")
    @Test
    void clearFailTableEmpty() {
        // given
        final CustomEventPublisher<OrderTable> eventPublisher = new FakeOrderTableEventPublisher();
        final OrderTable table = OrderTableFixture.of();

        // when
        final ThrowableAssert.ThrowingCallable request = () -> table.clear(eventPublisher);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable table = OrderTableFixture.of();

        // when
        final int expectedNumberOfGuests = 4;
        table.serve(new NumberOfGuests(expectedNumberOfGuests));

        // then
        assertAll(
            () -> assertThat(table.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
            () -> assertThat(table.isEmpty()).isFalse()
        );
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTableEmpty() {
        // given
        final OrderTable table = OrderTableFixture.of();

        // when
        final ThrowableAssert.ThrowingCallable request = () ->
            table.changeNumberOfGuests(new NumberOfGuests(4));

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블에 방문한 손님 수는 0보다 작을 수 없다.")
    @Test
    void changeNumberOfGuestsFailInvalidNumberOfGuests() {
        // given
        final OrderTable table = OrderTableFixture.of();

        // when
        final ThrowableAssert.ThrowingCallable request = () ->
            table.changeNumberOfGuests(new NumberOfGuests(Integer.MIN_VALUE));

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체로 지정될 수 있다.")
    @Test
    void group() {
        // given
        final OrderTable table = OrderTableFixture.of(1L);
        final TableGroup tableGroup = TableGroupFixture.of(1L);

        // when
        table.group(tableGroup);

        // then
        assertThat(table.getTableGroupId()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("이미 단체 지정된 테이블은 단체 지정될 수 없다.")
    @Test
    void groupFailAlreadyGrouped() {
        // given
        final OrderTable table = OrderTableFixture.of(1L);
        final TableGroup tableGroup = TableGroupFixture.of(1L);
        table.group(tableGroup);

        // when
        final ThrowableAssert.ThrowingCallable request = () -> table.group(tableGroup);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("단체에서 제외될 수 있다.")
    @Test
    void ungroup() {
        // given
        final OrderTable table = OrderTableFixture.of(1L);
        final TableGroup tableGroup = TableGroupFixture.of(1L);
        table.group(tableGroup);

        // when
        table.ungroup();

        // then
        assertThat(table.getTableGroupId()).isEqualTo(0L);
    }
}
