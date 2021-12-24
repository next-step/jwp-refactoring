package kitchenpos.table.application;

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
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.common.event.GroupingOrderTableEvent;
import kitchenpos.common.event.UngroupOrderTableEvent;
import kitchenpos.common.vo.TableGroupId;

@ExtendWith(MockitoExtension.class)
public class GroupingTableHandlerTest {
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    GroupingTableHandler groupingTableHandler;

    @DisplayName("단체지정ID와 주문테이블ID들로 테이블 그룹 이벤트가 발생시 주문테이블을 단체지정 ID로 그룹화된다.")
    @Test
    void event_groupingOrderTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);
                
        GroupingOrderTableEvent groupingOrderTableEvent = new GroupingOrderTableEvent(1L, List.of(1L, 2L));

        when(orderTableRepository.findByIdIn(anyList())).thenReturn(List.of(주문테이블, 주문테이블2));
        
        // when
        groupingTableHandler.handle(groupingOrderTableEvent);

        // then
        Assertions.assertThat(주문테이블.getTableGroupId()).isEqualTo(TableGroupId.of(1L));
        Assertions.assertThat(주문테이블2.getTableGroupId()).isEqualTo(TableGroupId.of(1L));
    }


    @DisplayName("단체지정ID와 주문테이블ID들로 테이블 그룹 이벤트가 발생시 주문테이블의 단체지정이 해제된다.")
    @Test
    void event_ungroupOrderTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);
        
        주문테이블.groupingTable(TableGroupId.of(1L));
        주문테이블2.groupingTable(TableGroupId.of(1L));
        
        UngroupOrderTableEvent ungroupOrderTableEvent = new UngroupOrderTableEvent(1L, List.of(1L, 2L));

        when(orderTableRepository.findByIdIn(anyList())).thenReturn(List.of(주문테이블, 주문테이블2));
        
        // when
        groupingTableHandler.handle(ungroupOrderTableEvent);

        // then
        Assertions.assertThat(주문테이블.getTableGroupId()).isNull();
        Assertions.assertThat(주문테이블2.getTableGroupId()).isNull();
    }
}
