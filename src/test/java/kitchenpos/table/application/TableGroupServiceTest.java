package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private TableService tableService;
    
    @Mock
    private TableGroupRepository tableGroupRepository;
    
    @Mock
    private TableValidator tableValidator;

    @InjectMocks
    private TableGroupService tableGroupService;
    
    @DisplayName("단체지정을 등록할 수 있다")
    @Test
    void 단체지정_등록() {
        // given
        List<OrderTable> 테이블_목록 = new ArrayList<OrderTable>();
        테이블_목록.add(OrderTable.of(3, true));
        테이블_목록.add(OrderTable.of(5, true));
        TableGroup 단체지정 = TableGroup.from(테이블_목록);
        
        given(tableService.findByOrderTables(anyList())).willReturn(단체지정.getOrderTables().getOrderTables());
        given(tableGroupRepository.save(any())).willReturn(단체지정);
        
        // when
        TableGroupResponse 저장된_단체지정 = tableGroupService.create(TableGroupRequest.from(단체지정));
        
        // then
        assertThat(저장된_단체지정).isEqualTo(TableGroupResponse.from(단체지정));
    }
    
    

    @DisplayName("단체지정을 해제할 수 있다")
    @Test
    void 단체지정_해제() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(3, false);
        OrderTable 두번째_테이블 = OrderTable.of(5, false);
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        given(tableGroupRepository.findByIdWithOrderTable(nullable(Long.class))).willReturn(Optional.of(단체지정));
        doNothing().when(tableValidator).checkIsCookingOrMeal(nullable(Long.class));
    
        // when
        tableGroupService.ungroup(단체지정.getId());
    
        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroup()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroup()).isNull()
                );
    }
    
}
