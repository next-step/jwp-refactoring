package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderTableRepository orderTableRepository;
    
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;
    
    @DisplayName("단체지정을 등록할 수 있다")
    @Test
    void 단체지정_등록() {
        // given
        TableGroup 단체지정 = TableGroup.from(new ArrayList<OrderTable>());
        OrderTable 첫번째_테이블 = OrderTable.of(null, 3, true);
        OrderTable 두번째_테이블 = OrderTable.of(null, 5, true);
        단체지정.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(단체지정.getOrderTables());
        given(tableGroupRepository.save(단체지정)).willReturn(단체지정);
        
        // when
        TableGroup 저장된_단체지정 = tableGroupService.create(단체지정);
        
        // then
        assertThat(저장된_단체지정).isEqualTo(단체지정);
    }
    
    @DisplayName("주문 테이블이 두 테이블 이상이어야 단체지정을 할 수 있다 - 예외처리")
    @Test
    void 단체지정_등록_두_테이블_이상만() {
        // given
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(OrderTable.of(null, 3, true)));
    
        // when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("단체지정은 최소 두 테이블 이상만 가능합니다");
    }
    
    @DisplayName("단체지정 등록시 주문 테이블은 미리 등록되어 있어야한다 - 예외처리")
    @Test
    void 단체지정_등록_등록된_주문테이블만() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(null, 3, true);
        OrderTable 두번째_테이블 = OrderTable.of(null, 5, true);
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        // when
        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Arrays.asList());
    
        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 주문 테이블만 단체지정 할 수 있습니다");
    }
    
    @DisplayName("단체지정을 해제할 수 있다")
    @Test
    void 단체지정_해제() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(null, 3, false);
        OrderTable 두번째_테이블 = OrderTable.of(null, 5, false);
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        첫번째_테이블.setTableGroup(단체지정);
        두번째_테이블.setTableGroup(단체지정);
        
        given(orderTableRepository.findAllByTableGroupId(nullable(Long.class))).willReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));
        given(orderTableRepository.save(첫번째_테이블)).willReturn(첫번째_테이블);
        given(orderTableRepository.save(두번째_테이블)).willReturn(두번째_테이블);
    
        // when
        tableGroupService.ungroup(단체지정.getId());
    
        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroup()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroup()).isNull()
                );
    }
    
    @DisplayName("단체지정 해제시 조리중, 식사중인 주문 테이블이 있다면 해제할 수 없다 - 예외처리")
    @Test
    void 단체지정_해제_조리중_식사중인_테이블_불가() {
        // given
        TableGroup 단체지정 = TableGroup.from(new ArrayList<OrderTable>());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
    
        // when, then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정.getId());
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 단체지정을 해제할 수 업습니다");
    }
    
}
