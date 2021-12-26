package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 그룹 도메인 테스트")
public class TableGroupTest {
    @DisplayName("테이블 그룹 추가")
    @Test
    void 테이블_그룹_추가() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable firstOrderTable = OrderTable.of(5, true);
        OrderTable secondOrderTable = OrderTable.of(5, true);

        // when
        tableGroup.group(Arrays.asList(firstOrderTable, secondOrderTable));

        // then
        assertAll(
                () -> assertThat(firstOrderTable.getTableGroup()).isNotNull(),
                () -> assertThat(secondOrderTable.getTableGroup()).isNotNull()
        );
    }

    @DisplayName("빈 주문 테이블 그룹 추가 예외")
    @Test
    void 빈_주문_테이블_그룹_추가_예외() {
        // given
        TableGroup tableGroup = TableGroup.create();

        // when
        Throwable thrown = catchThrowable(() -> tableGroup.group(new ArrayList<>()));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문 테이블이 존재하지 않아 그룹화할 수 없습니다.");
    }

    @DisplayName("두개 미만의 주문 테이블 그룹 추가 예외")
    @Test
    void 두개미만_주문_테이블_그룹_추가_예외() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable orderTable = OrderTable.of(5, false);

        // when
        Throwable thrown = catchThrowable(() -> tableGroup.group(Collections.singletonList(orderTable)));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문 테이블 2개 이상 그룹화할 수 있습니다.");
    }

    @DisplayName("비어 있지 않은 테이블 그룹 추가 예외")
    @Test
    void 비어_있지_않은_테이블_그룹_추가_예외() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable firstOrderTable = OrderTable.of(5, true);
        OrderTable secondOrderTable = OrderTable.of(5, false);

        // when
        Throwable thrown = catchThrowable(() -> tableGroup.group(Arrays.asList(firstOrderTable, secondOrderTable)));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("빈 테이블이 아닌 주문 테이블은 그룹화할 수 없습니다.");
    }

    @DisplayName("이미 그룹화된 테이블 그룹 추가 예외")
    @Test
    void 이미_그룹화된_테이블_그룹_추가_예외() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable firstOrderTable = OrderTable.of(5, true);
        OrderTable secondOrderTable = OrderTable.of(5, true);
        secondOrderTable.addGroup(TableGroup.create());

        // when
        Throwable thrown = catchThrowable(() -> tableGroup.group(Arrays.asList(firstOrderTable, secondOrderTable)));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("이미 그룹화된 주문 테이블 입니다.");
    }

    @DisplayName("그룹 해제")
    @Test
    void 그룹_해제() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable firstOrderTable = OrderTable.of(5, true);
        OrderTable secondOrderTable = OrderTable.of(5, true);

        tableGroup.group(Arrays.asList(firstOrderTable, secondOrderTable));

        Order firstOrder = Order.of(firstOrderTable);
        Order secondOrder = Order.of(secondOrderTable);
        firstOrder.changeOrderStatus(OrderStatus.COMPLETION);
        secondOrder.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        tableGroup.ungroup();

        // then
        assertAll(
                () -> assertThat(firstOrderTable.getTableGroup()).isNull(),
                () -> assertThat(secondOrderTable.getTableGroup()).isNull()
        );
    }

    @DisplayName("완료되지 않은 그룹 해제 예외")
    @Test
    void 완료되지_않은_그룹_해제_예외() {
        // given
        TableGroup tableGroup = TableGroup.create();
        OrderTable firstOrderTable = OrderTable.of(5, true);
        OrderTable secondOrderTable = OrderTable.of(5, true);

        tableGroup.group(Arrays.asList(firstOrderTable, secondOrderTable));

        Order.of(firstOrderTable);
        Order.of(secondOrderTable);

        // when
        Throwable thrown = catchThrowable(tableGroup::ungroup);

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문이 완료 되지 않아 그룹을 해제할 수 없습니다.");
    }
}
