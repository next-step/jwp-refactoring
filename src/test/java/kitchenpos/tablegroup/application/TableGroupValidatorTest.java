package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.tablegroup.exception.CannotUngroupException;

@DisplayName("단체지정 유효성 검증 테스트")
class TableGroupValidatorTest {
    private final TableGroupValidator tableGroupValidator = new TableGroupValidator();

    @Test
    @DisplayName("단체지정 해지 불가 대상 확인")
    void validate_existsOrdersStatus_is_cookingOrMeal() {
        Order order = new Order(LocalDateTime.now(), 1L);
        order.changeOrderStatus(OrderStatus.MEAL);

        assertThatThrownBy(() -> tableGroupValidator.validateExistsOrdersStatusIsCookingOrMeal(Arrays.asList(order)))
                .isInstanceOf(CannotUngroupException.class)
                .hasMessageContaining("단체지정 해지를 할 수 없는 주문상태가 존재합니다.")
        ;
    }

    @TestFactory
    @DisplayName("단체지정 대상 유효성 검증 로직 테스트")
    List<DynamicTest> orderTable_is_empty_or_hasTableGroups() {
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, false);
        OrderTable orderTable3 = new OrderTable(3, true);
        return Arrays.asList(
                dynamicTest("단체지정 테이블 중 비어있지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    orderTable1.ungroup();
                    orderTable1.changeEmpty(false);

                    // then
                    assertThatThrownBy(() -> tableGroupValidator.validateOrderTableIsEmptyOrHasTableGroups(Arrays.asList(orderTable1, orderTable2)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
                }),
                dynamicTest("단체지정 테이블 중 테이블 그룹이 지정되어 있는 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    orderTable1.groupBy(1L);

                    // then
                    assertThatThrownBy(() -> tableGroupValidator.validateOrderTableIsEmptyOrHasTableGroups(Arrays.asList(orderTable1, orderTable2)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
                })
        );
    }

    @TestFactory
    @DisplayName("단체지정 등록 오류")
    List<DynamicTest> group_exception() {
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, false);
        return Arrays.asList(
                dynamicTest("단체지정 테이블이 2개 이상이 아닐 경우 오류 발생.", () -> {
                    // then
                    assertThatThrownBy(() -> tableGroupValidator.validateOrderTablesConditionForCreatingTableGroup(Arrays.asList(orderTable1), 2))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
                }),
                dynamicTest("단체지정 테이블 중 등록되지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // then
                    assertThatThrownBy(() -> tableGroupValidator.validateOrderTablesConditionForCreatingTableGroup(Arrays.asList(orderTable1, orderTable2), 3))
                            .isInstanceOf(MisMatchedOrderTablesSizeException.class)
                            .hasMessage("입력된 항목과 조회결과가 일치하지 않습니다.");
                })
        );
    }
}
