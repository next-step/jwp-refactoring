package kitchenpos.application.tablegroup;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.tablegroup.TableGroupDto;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.NotRegistedMenuOrderTableException;

@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {
    @Mock
    OrderService orderService;

    @Mock
    TableService tableService;

    @InjectMocks
    TableGroupValidator tableGroupValidator;

    @DisplayName("그룹해제시 주문상태가 전부 계산완료가아니면 예외가 발생된다.")
    @Test
    void exception_unGroup_notCompletionOrderStatus() {
        // given
        OrderTables 주문테이블들 = OrderTables.of(List.of(OrderTable.of(0, true), OrderTable.of(10, false)));

        when(orderService.hasNotComplateStatus(anyList())).thenReturn(true);

        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                    .isThrownBy(() -> tableGroupValidator.validateForUnGroup(주문테이블들));
    }

    @DisplayName("단체지정에대한 주문상태가 전부 계산완료인 주문테이블이 조회된다.")
    @Test
    void find_allComplateOrderTable() {
        // given
        OrderTables 주문테이블들 = OrderTables.of(List.of(OrderTable.of(10, false), OrderTable.of(10, false)));
        
        when(tableService.findByTableGroupId(anyLong())).thenReturn(List.of(OrderTable.of(10, false), OrderTable.of(10, false)));
        when(orderService.hasNotComplateStatus(anyList())).thenReturn(false);

        OrderTables 조회된_주문테이블들 = tableGroupValidator.getComplateOrderTable(1L);

        // when
        // then
        Assertions.assertThat(조회된_주문테이블들).isEqualTo(주문테이블들);
    }

    @DisplayName("미존재 주문테이블가 포함된 단체지정으로 저장시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_containNotExistOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블);

        when(tableService.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotRegistedMenuOrderTableException.class)
                    .isThrownBy(() -> tableGroupValidator.getValidatedOrderTables(TableGroupDto.of(List.of(OrderTableDto.of(2), OrderTableDto.of(3)))));
    }
}
