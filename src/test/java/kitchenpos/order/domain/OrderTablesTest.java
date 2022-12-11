package kitchenpos.order.domain;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 집합 관련 도메인 테스트")
public class OrderTablesTest {

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, null, 5, true);
        주문테이블B = generateOrderTable(2L, null, 4, true);
    }

    @DisplayName("주문 테이블 집합을 생성한다.")
    @Test
    void createOrderTables() {
        // when
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블A, 주문테이블B));

        // then
        assertAll(
                () -> assertThat(orderTables.unmodifiableOrderTables()).hasSize(2),
                () -> assertThat(orderTables.unmodifiableOrderTables()).containsExactly(주문테이블A, 주문테이블B)
        );
    }

    @DisplayName("주문 테이블 집합 내 주문 테이블들의 그룹 상태를 해제하면, 각 주문 테이블의 그룹은 null이 된다.")
    @Test
    void ungroupOrderTables() {
        // given
        TableGroup tableGroup = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        OrderTables orderTables = tableGroup.getOrderTables();

        // when
        orderTables.ungroupOrderTables();

        // then
        assertThat(orderTables.unmodifiableOrderTables().stream().map(OrderTable::getTableGroup))
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

    @DisplayName("주문 테이블 집합 내 이미 단체가 지정된 주문 테이블이 있다면 참을 반환한다.")
    @Test
    void orderTablesAnyHasGroupId() {
        // given
        TableGroup tableGroup = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        OrderTables orderTables = tableGroup.getOrderTables();

        // when
        boolean anyHasGroupId = orderTables.anyHasGroupId();

        // then
        assertThat(anyHasGroupId).isTrue();
    }

    @DisplayName("주문 테이블 집합의 단체를 지정한다.")
    @Test
    void updateTableGroupInOrderTables() {
        // given
        TableGroup tableGroup = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        OrderTable 주문테이블C = generateOrderTable(3L, null, 5, true);
        OrderTable 주문테이블D = generateOrderTable(4L, null, 5, true);
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블C, 주문테이블D));

        // when
        orderTables.updateTableGroup(tableGroup);

        // then
        assertAll(
                () -> assertThat(주문테이블C.getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(주문테이블D.getTableGroup()).isNotNull()
        );
    }
}
