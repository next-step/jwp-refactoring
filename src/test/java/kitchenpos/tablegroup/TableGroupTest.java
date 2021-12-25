package kitchenpos.tablegroup;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정")
class TableGroupTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;



    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        OrderTable firstOrderTable = OrderTable.builder().empty(false).build();
        OrderTable secondOrderTable = OrderTable.builder().empty(false).build();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final TableGroup tableGroup = TableGroup.builder().build();
            tableGroup.saveOrderTable(firstOrderTable);
            tableGroup.saveOrderTable(secondOrderTable);
        });
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        OrderTable firstOrderTable = OrderTable.builder().empty(true).build();
        OrderTable secondOrderTable = OrderTable.builder().empty(false).build();
        final TableGroup existTableGroup = TableGroup.builder().build();
        existTableGroup.saveOrderTable(firstOrderTable);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final TableGroup tableGroup = TableGroup.builder().build();
            tableGroup.saveOrderTable(firstOrderTable);
            tableGroup.saveOrderTable(secondOrderTable);
        });
    }

    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 상태면 변경이 불가능하다.")
    void ungroupFailBecauseOfOrderStatusCookingOrMeal() {
        // given
        OrderTable firstOrderTable = OrderTable.builder().empty(true).build();
        OrderTable secondOrderTable = OrderTable.builder().empty(true).build();
        firstOrderTable.addOrder(Order.builder().orderStatus(OrderStatus.COOKING).build());
        secondOrderTable.addOrder(Order.builder().orderStatus(OrderStatus.COMPLETION).build());

        final TableGroup tableGroup = TableGroup.builder().build();
        tableGroup.saveOrderTable(firstOrderTable);
        tableGroup.saveOrderTable(secondOrderTable);


        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroup.getOrderTables().forEach(orderTable -> {
                orderTable.ungroup();
            });
        });
    }
}
