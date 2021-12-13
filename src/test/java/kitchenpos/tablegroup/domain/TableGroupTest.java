package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 그룹 도메인 테스트")
class TableGroupTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(true);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void of() {
        // given
        LocalDateTime createdDate = LocalDateTime.now();

        // when
        TableGroup tableGroup = TableGroup.of(createdDate, Collections.singletonList(orderTable));

        // then
        assertAll(
                () -> assertThat(tableGroup.getCreatedDate()).isEqualTo(createdDate),
                () -> assertThat(tableGroup.getOrderTables()).containsExactly(orderTable)
        );
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void ofThrowException1() {
        // given
        OrderTable orderTable = new OrderTable(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.of(LocalDateTime.now(), Collections.singletonList(orderTable)));
    }

    @Test
    @DisplayName("이미 테이블 그룹에 등록된 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void ofThrowException2() {
        // given
        TableGroup.of(LocalDateTime.now(), Collections.singletonList(orderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.of(LocalDateTime.now(), Collections.singletonList(orderTable)));
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void ungroup() {
        // given
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), Collections.singletonList(orderTable));

        // when
        tableGroup.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"COOKING", "MEAL"}, delimiter = ':')
    @DisplayName("테이블 그룹에서 주문 완료 상태가 아닌 테이블을 제거하면 예외를 발생한다.")
    void ungroupThrowException(OrderStatus orderStatus) {
        // given
        Order order = new Order(orderStatus, LocalDateTime.now());
        order.changeOrderTable(orderTable);
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), Collections.singletonList(orderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(tableGroup::ungroup);
    }
}
