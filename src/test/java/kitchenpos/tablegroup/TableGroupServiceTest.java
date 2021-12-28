package kitchenpos.tablegroup;

import kitchenpos.AcceptanceTest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.exception.TableGroupNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정 관련 기능")
class TableGroupServiceTest extends AcceptanceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void createTableGroupFailBecauseOfNotExistTable() {
        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(1L), new TableGroupCreateRequest.OrderTable(2L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(0, false));
        OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(0, false));


        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()), new TableGroupCreateRequest.OrderTable(secondOrderTable.getId()))));
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(1L,0, true));
        OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(1L, 0, true));

        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()), new TableGroupCreateRequest.OrderTable(secondOrderTable.getId()))));
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

/*    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 상태면 변경이 불가능하다.")
    void ungroupFailBecauseOfOrderStatusCookingOrMeal() {
        // given
        OrderTable firstOrderTable = new OrderTable(0, true);
        OrderTable secondOrderTable = new OrderTable(0, true);
        firstOrderTable.addOrder(new Order(OrderStatus.COOKING));
        secondOrderTable.addOrder(new Order(OrderStatus.COMPLETION));

        final TableGroup tableGroup = TableGroup.builder().build();
        tableGroup.saveOrderTable(firstOrderTable);
        tableGroup.saveOrderTable(secondOrderTable);


        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroup.getOrderTables().forEach(orderTable -> {
                orderTable.ungroup();
            });
        });
    }*/
}
