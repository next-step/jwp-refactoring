package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    
    @Mock
    private OrderTableDao orderTableDao;
    
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;
    
    @DisplayName("단체지정을 등록할 수 있다")
    @Test
    void 단체지정_등록() {
        // given
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        
        OrderTable 첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
        첫번째_테이블.setNumberOfGuests(3);
        첫번째_테이블.setEmpty(true);
        
        OrderTable 두번째_테이블 = new OrderTable();
        두번째_테이블.setId(2L);
        두번째_테이블.setNumberOfGuests(5);
        두번째_테이블.setEmpty(true);
        
        단체지정.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(단체지정.getOrderTables());
        given(tableGroupDao.save(단체지정)).willReturn(단체지정);
        
        // when
        TableGroup 저장된_단체지정 = tableGroupService.create(단체지정);
        
        // then
        assertThat(저장된_단체지정).isEqualTo(단체지정);
    }
    
    @DisplayName("주문 테이블이 두 테이블 이상이어야 단체지정을 할 수 있다 - 예외처리")
    @Test
    void 단체지정_등록_두_테이블_이상만() {
        // given
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        단체지정.setOrderTables(Arrays.asList(new OrderTable()));
    
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
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        단체지정.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        
        // when
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList());
    
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
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        
        OrderTable 첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
        첫번째_테이블.setEmpty(false);
        첫번째_테이블.setTableGroupId(단체지정.getId());
        
        OrderTable 두번째_테이블 = new OrderTable();
        두번째_테이블.setId(2L);
        두번째_테이블.setEmpty(false);
        두번째_테이블.setTableGroupId(단체지정.getId());
        
        단체지정.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));
        given(orderTableDao.save(첫번째_테이블)).willReturn(첫번째_테이블);
        given(orderTableDao.save(두번째_테이블)).willReturn(두번째_테이블);

        // when
        tableGroupService.ungroup(단체지정.getId());

        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroupId()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroupId()).isNull()
                );
    }
    
    @DisplayName("단체지정 해제시 조리중, 식사중인 주문 테이블이 있다면 해제할 수 없다 - 예외처리")
    @Test
    void 단체지정_해제_조리중_식사중인_테이블_불가() {
        // given
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정.getId());
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 단체지정을 해제할 수 업습니다");
    }
    
}
