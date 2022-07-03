package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.testfixture.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTableTest {
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;
    private OrderTable 단체지정_테이블;
    private OrderTable 빈_테이블;

    @BeforeEach
    void setUp() {
        // given
        orderTables = Arrays.asList(
                createOrderTable(1L, null, 0, true),
                createOrderTable(2L, null, 0, true)
        );
        tableGroup = createTableGroup(orderTables);
        단체지정_테이블 = createOrderTable(1L, tableGroup, 4, false);
        빈_테이블 = createOrderTable(1L, null, 0, true);
    }

    @Test
    @DisplayName("테이블 단체지정 후 값 비교 확인")
    void setTableGroup() {
        빈_테이블.setTableGroup(tableGroup);

        // then
        assertAll(
                () -> assertThat(빈_테이블.isEmpty()).isFalse(),
                () -> assertThat(빈_테이블.getTableGroup()).isEqualTo(tableGroup)
        );
    }

    @Test
    @DisplayName("주문 테이블이 단체지정된 테이블이면 true 반환하는지 확인")
    void isGrouped() {
        // then
        assertAll(
                () -> assertThat(단체지정_테이블.isGrouped()).isTrue(),
                () -> assertThat(빈_테이블.isGrouped()).isFalse()
        );
    }

    @Test
    @DisplayName("이미 단체지정된 테이블이면 Exception 발생 확인")
    void validateHasTableGroupId() {
        // then
        assertThatThrownBy(() -> {
            단체지정_테이블.validateHasTableGroupId();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블이면 Exception 발생 확인")
    void validateIsEmpty() {
        // then
        assertThatThrownBy(() -> {
            빈_테이블.validateIsEmpty();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블로 변경 후 empty 값 true 인지 확인")
    void updateEmpty() {
        // when
        단체지정_테이블.updateEmpty();

        // then
        assertThat(단체지정_테이블.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("단체지정 해제 tableGroup null 인지 확인")
    void unGroup() {
        // when
        단체지정_테이블.unGroup();

        // then
        assertThat(단체지정_테이블.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("방문객 수 변경 후 방문객 수 확인")
    void changeNumberOfGuests() {
        // when
        단체지정_테이블.changeNumberOfGuests(6);

        // then
        assertThat(단체지정_테이블.getNumberOfGuests()).isEqualTo(6);
    }
}
