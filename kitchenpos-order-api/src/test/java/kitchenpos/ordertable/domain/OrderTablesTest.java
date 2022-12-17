package kitchenpos.ordertable.domain;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.ordertable.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.tablegroup.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 집합 관련 도메인 테스트")
public class OrderTablesTest {

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, 5, true);
        주문테이블B = generateOrderTable(2L, 4, true);
    }

    @DisplayName("주문 테이블 집합을 생성한다.")
    @Test
    void createOrderTables() {
        // when
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블A, 주문테이블B));

        // then
        assertAll(
                () -> Assertions.assertThat(orderTables.findOrderTables()).hasSize(2),
                () -> Assertions.assertThat(orderTables.findOrderTables()).containsExactly(주문테이블A, 주문테이블B)
        );
    }

    @DisplayName("주문 테이블 집합 내 주문 테이블들의 그룹 상태를 해제하면, 각 주문 테이블의 그룹은 null이 된다.")
    @Test
    void ungroupOrderTables() {
        // given
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블A, 주문테이블B));

        // when
        orderTables.ungroupOrderTables();

        // then
        Assertions.assertThat(orderTables.findOrderTables().stream().map(OrderTable::findTableGroupId))
                .containsExactly(null, null);
    }

    @DisplayName("주문 테이블 집합 내 주문 테이블이 2개 미만이면 주문 테이블 집합 생성 시 에러가 발생한다.")
    @Test
    void createOrderTablesThrowErrorWhenOrderTableCountIsSmallerThanTwo() {
        // when & then
        assertThatThrownBy(() -> OrderTables.from(singletonList(주문테이블A)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_테이블은_2개_이상여야함.getErrorMessage());
    }

    @DisplayName("주문 테이블 집합 내 주문 테이블이 없으면 주문 테이블 집합 생성 시 에러가 발생한다.")
    @Test
    void createOrderTablesThrowErrorWhenOrderTableIsEmpty() {
        // when & then
        assertThatThrownBy(() -> OrderTables.from(emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_테이블_집합은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("주문 테이블 집합의 단체를 지정한다.")
    @Test
    void updateTableGroupInOrderTables() {
        // given
        TableGroup tableGroup = TableGroupTestFixture.generateTableGroup(1L, Arrays.asList(주문테이블A, 주문테이블B));
        OrderTable 주문테이블C = generateOrderTable(3L, 5, true);
        OrderTable 주문테이블D = generateOrderTable(4L, 5, true);
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블C, 주문테이블D));

        // when
        orderTables.registerTableGroup(tableGroup.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(주문테이블C.findTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> Assertions.assertThat(주문테이블D.findTableGroupId()).isNotNull()
        );
    }
}
