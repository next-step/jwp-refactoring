package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 그룹 도메인 테스트")
class TableGroupTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(true);
        orderTable2 = new OrderTable(true);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void of() {
        // when
        TableGroup tableGroup = TableGroup.of(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThat(tableGroup.getOrderTables())
                .isEqualTo(new OrderTables(Arrays.asList(orderTable1, orderTable2)));
    }

    @Test
    @DisplayName("1개 이하의 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void ofThrowException1() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.of(Collections.singletonList(orderTable1)))
                .withMessageMatching(TableGroup.MESSAGE_VALIDATE);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void ofThrowException2() {
        // given
        OrderTable orderTable3 = new OrderTable(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.of(Arrays.asList(orderTable1, orderTable3)))
                .withMessageMatching(TableGroup.MESSAGE_VALIDATE_ORDER_TABLE);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 등록된 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void ofThrowException3() {
        // given
        TableGroup.of(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.of(Arrays.asList(orderTable1, orderTable2)))
                .withMessageMatching(TableGroup.MESSAGE_VALIDATE_ORDER_TABLE);
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void ungroup() {
        // given
        TableGroup tableGroup = TableGroup.of(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroup.ungroup();

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
    }
}
