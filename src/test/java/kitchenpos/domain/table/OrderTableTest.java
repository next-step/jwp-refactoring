package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.domain.tablegroup.TableGroup;

class OrderTableTest {

    private TableGroup 단체_테이블그룹;

    @BeforeEach
    void setUp() {
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);
    }

    @DisplayName("OrderTable 은 NumberOfGuests 로 생성된다.")
    @ParameterizedTest
    @CsvSource(value = {"0, true", "1, false", "2, false", "10, false"})
    void create1(int numberOfGuests, boolean empty) {
        // when
        OrderTable orderTable = OrderTable.from(numberOfGuests);

        // then
        assertAll(
            () -> assertEquals(orderTable.getNumberOfGuests(), NumberOfGuests.from(numberOfGuests)),
            () -> assertEquals(orderTable.isEmpty(), empty)
        );
    }

    @DisplayName("OrderTalbe 생성 시, NumberOfGuests 가 0 미만의 음수이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create2(int numberOfGuests) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderTable.from(numberOfGuests))
                                            .withMessageContaining("NumberOfGuests 는 0 이상의 숫자로 생성할 수 있습니다.");
    }

    @DisplayName("OderTable 의 NumberOfGuests 를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100})
    void updateNumberOfGuests1(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.from(0);

        // when
        orderTable.updateNumberOfGuests(numberOfGuests);

        // then
        assertEquals(orderTable.getNumberOfGuests(), NumberOfGuests.from(numberOfGuests));
    }

    @DisplayName("OderTable 의 NumberOfGuests 를 0 미만의 음수로 변경 시, 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void updateNumberOfGuests2(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.from(0);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
                                            .withMessageContaining("NumberOfGuests 는 0 이상의 숫자로 생성할 수 있습니다.");
    }

    @DisplayName("OrderTable 은 TableGroup 을 할당 할 수 있다.")
    @Test
    void alignTableGroup() {
        // given
        OrderTable orderTable = OrderTable.from(0);

        // when
        orderTable.alignTableGroup(단체_테이블그룹);

        // then
        assertAll(
            () -> assertTrue(orderTable.hasTableGroup()),
            () -> assertEquals(orderTable.getTableGroup(), 단체_테이블그룹)
        );
    }

    @DisplayName("OrderTable 은 TableGroup 을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable = OrderTable.from(0);
        orderTable.alignTableGroup(단체_테이블그룹);

        // when
        orderTable.ungroup();

        // then
        assertFalse(orderTable.hasTableGroup());
    }
}