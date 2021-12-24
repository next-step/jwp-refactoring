package kitchenpos.domain;

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

import kitchenpos.common.vo.TableGroupId;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NotEmptyOrderTableException;
import kitchenpos.table.exception.NotGroupingOrderTableCountException;
import kitchenpos.table.exception.NotRegistedMenuOrderTableException;

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

    @DisplayName("단체지정 유효성검사자는 주문테이블이 그룹하는 유효성을 만족할시 주문테이블들이 생성된다.")
    @Test
    void generate_validatedOrderTable_ForGrouping() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);
        
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블);

        when(tableService.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        OrderTables orderTables = tableGroupValidator.getValidatedOrderTables(TableGroupDto.of(List.of(OrderTableDto.of(2), OrderTableDto.of(3))));

        // then
        Assertions.assertThat(orderTables).isEqualTo(OrderTables.of(조회된_주문테이블_리스트));
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

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGroup_underTwoCountOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블);

        when(tableService.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotGroupingOrderTableCountException.class)
                    .isThrownBy(() -> tableGroupValidator.getValidatedOrderTables(TableGroupDto.of(List.of(OrderTableDto.of(2)))));
    }

    @DisplayName("단체지정 될 주문테이블이 이미 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGroup_existOrderTableInOtherTableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨3_주문_단체테이블 =  OrderTable.of(0, true);

        TableGroup 단체지정테이블 = TableGroup.of();
        치킨_주문_단체테이블.groupingTable(TableGroupId.of(단체지정테이블));
        치킨3_주문_단체테이블.groupingTable(TableGroupId.of(단체지정테이블));

        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블);

        when(tableService.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> tableGroupValidator.getValidatedOrderTables(TableGroupDto.of(List.of(OrderTableDto.of(2), OrderTableDto.of(3)))));
    }

    @DisplayName("빈테이블이 아닌 주문테이블 포함된 단체지정은 에러가 발생된다.")
    @Test
    void exception_createTableGroup_existEmptyOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블);

        
        when(tableService.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotEmptyOrderTableException.class)
                    .isThrownBy(() -> tableGroupValidator.getValidatedOrderTables(TableGroupDto.of(List.of(OrderTableDto.of(2), OrderTableDto.of(3)))));
    }
}
