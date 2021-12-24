package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블들 테스트")
class OrderTablesTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));
    }

    @DisplayName("주문 테이블들 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // when
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable));

        // then
        assertAll(
                () -> assertThat(orderTables).isNotNull()
                , () -> assertThat(orderTables.getOrderTables()).isEqualTo(Arrays.asList(orderTable))
        );
    }

    @DisplayName("그룹 테이블화 검증 테스트 - 테이블 비어 있음")
    @Test
    void validateGroup_validateGroupTable_isNotEmpty() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(true));
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTables.validateGroup(Collections.emptyList()));
    }

    @DisplayName("그룹 테이블화 검증 테스트 - 테이블 그룹에 이미 속해 있음")
    @Test
    void validateGroup_validateGroupTable_noNull() {
        // given
        OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), Empty.of(true));
        orderTable.setTableGroup(TableGroup.of(LocalDateTime.now()));
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTables.validateGroup(Collections.emptyList()));
    }

    @DisplayName("그룹 테이블화 검증 테스트 - 주문 테이블 수 일치하지 않음")
    @Test
    void validateGroup_validateSize() {
        // given
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTables.validateGroup(Collections.emptyList()));
    }
}
