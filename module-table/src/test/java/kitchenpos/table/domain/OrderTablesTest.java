package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.table.application.TableServiceTest.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("주문 테이블의 수가 2개 이상이어야 합니다.")
    @Test
    void validateForCreateTableGroup_exception1() {
        // given
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, false)
                ));

        OrderTables savedOrderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, false)
                ));

        // when && then
        assertThatThrownBy(() -> orderTables.validateForCreateTableGroup(savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유효하지 않은 주문 테이블이 존재 합니다.")
    @Test
    void validateForCreateTableGroup_exception2() {
        // given
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, false),
                        주문_테이블_데이터_생성(2L, null, 2, false)
                ));
        OrderTables savedOrderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, false),
                        주문_테이블_데이터_생성(2L, null, 2, false),
                        주문_테이블_데이터_생성(2L, null, 2, false)
                ));

        // when && then
        assertThatThrownBy(() -> orderTables.validateForCreateTableGroup(savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니거나 이미 단체 지정된 테이블 입니다.")
    @Test
    void validateForCreateTableGroup_exception3() {
        // given
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, false),
                        주문_테이블_데이터_생성(2L, null, 2, false)
                ));
        OrderTables savedOrderTables = new OrderTables(
                Arrays.asList(
                        주문_테이블_데이터_생성(1L, null, 2, true),
                        주문_테이블_데이터_생성(2L, null, 2, false)
                ));

        // when && then
        assertThatThrownBy(() -> orderTables.validateForCreateTableGroup(savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
