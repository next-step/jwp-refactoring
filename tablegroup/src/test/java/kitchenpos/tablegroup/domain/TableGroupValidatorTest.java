package kitchenpos.tablegroup.domain;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NotEmptyOrderTableException;
import kitchenpos.table.exception.NotGroupingOrderTableCountException;
import kitchenpos.table.exception.NotRegistedMenuOrderTableException;

@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {
    @Mock
    OrderService orderService;

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

    @DisplayName("미존재 주문테이블가 포함된 단체지정으로 저장시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_containNotExistOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotRegistedMenuOrderTableException.class)
                    .isThrownBy(() -> tableGroupValidator.checkAllExistOfOrderTables(List.of(OrderTableDto.of(2), OrderTableDto.of(3)), OrderTables.of(조회된_주문테이블_리스트)));
    }

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGroup_underTwoCountOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotGroupingOrderTableCountException.class)
                    .isThrownBy(() -> tableGroupValidator.checkOrderTableSize(OrderTables.of(조회된_주문테이블_리스트)));
    }

    @DisplayName("단체지정 될 주문테이블이 이미 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGroup_existOrderTableInOtherTableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨3_주문_단체테이블 =  OrderTable.of(0, true);

        TableGroup 단체지정테이블 = TableGroup.of();
        치킨_주문_단체테이블.groupingTable(TableGroupId.of(단체지정테이블.getId()));
        치킨3_주문_단체테이블.groupingTable(TableGroupId.of(단체지정테이블.getId()));

        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> tableGroupValidator.checkHasTableGroup(치킨_주문_단체테이블));
    }

    @DisplayName("주문테이블이 빈테이블이 아니면 에러가 발생된다.")
    @Test
    void exception_createTableGroup_existEmptyOrderTable() {
        // given
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotEmptyOrderTableException.class)
                    .isThrownBy(() -> tableGroupValidator.checkNotEmptyTable(치킨2_주문_단체테이블));
    }
}
