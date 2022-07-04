package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table_group.application.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorImplTest {

    private TableGroupValidator tableGroupValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidatorImpl(orderRepository);
    }

    @DisplayName("조회되지 않는 주문 테이블이 있으면 생성할 수 없다.")
    @Test
    void checkCreatable_fail_notExistsTable() {
        //given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);

        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupValidator.checkCreatable(
                Arrays.asList(orderTable1), Arrays.asList(orderTableId1, orderTableId2)));
    }

    @DisplayName("테이블이 2개 미만이면 그룹핑할 수 없다.")
    @Test
    void checkCreatable_fail_low2() {
        //given
        Long orderTableId = 1L;
        OrderTable orderTable1 = new OrderTable(orderTableId, null, 4, true);

        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupValidator.checkCreatable(
                Arrays.asList(orderTable1), Arrays.asList(orderTableId)));
    }

    @DisplayName("비지 테이블이 있으면 그룹핑할 수 없다.")
    @Test
    void checkCreatable_fail_notEmptyTable() {
        //given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, false);

        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupValidator.checkCreatable(
                Arrays.asList(orderTable1, orderTable2), Arrays.asList(orderTableId1, orderTableId2)));
    }

    @DisplayName("주문상태가 조리나 식사이면 그룹 해제할 수 없다.")
    @Test
    void checkValidUngroup_fail_invalidOrderStatus() {
        //given
        Long tableId1 = 1L;
        Long tableId2 = 2L;

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
            .isThrownBy(() -> tableGroupValidator.checkValidUngroup(Arrays.asList(tableId1, tableId2)));
    }

    @DisplayName("요리|식사중인 주문이 있으면, 그룹핑을 해제할 수 없다.")
    @Test
    void checkValidUngroup_fail_orderStatus() {
        //given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(orderTableId1, orderTableId2), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
            .isThrownBy(() -> tableGroupValidator.checkValidUngroup(Arrays.asList(orderTableId1, orderTableId2)));
    }

}