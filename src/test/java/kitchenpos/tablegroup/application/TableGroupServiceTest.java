package kitchenpos.tablegroup.application;


import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.ServiceTestFactory.테이블그룹생성;
import static kitchenpos.common.ServiceTestFactory.테이블그룹요청생성;
import static kitchenpos.common.ServiceTestFactory.테이블생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("테이블그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable table1;
    private OrderTable table2;
    private List<Long> orderTablesIds;
    private TableGroupRequest tableGroupRequest;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        table1 = 테이블생성(3, true);
        table2 = 테이블생성(3, true);
        orderTablesIds = Arrays.asList(table1.getId(), table2.getId());
        tableGroupRequest = 테이블그룹요청생성(orderTablesIds);
        tableGroup = 테이블그룹생성(1L, new ArrayList<>(Arrays.asList(table1, table2)));
    }

    @Test
    void 테이블그룹을_생성할_수_있다() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(table1, table2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        TableGroupResponse result = tableGroupService.create(tableGroupRequest);

        assertThat(result).isNotNull();
    }

    @Test
    void 주문테이블이_2개미만이면_테이블그룹을_생성할_수_없다() {
        TableGroupRequest 테이블그룹_주문테이블1개 = 테이블그룹요청생성(Arrays.asList(table1.getId()));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(table1, table2));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_주문테이블1개))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_삭제할_수_있다() {
        OrderTable 그룹있는_테이블1 = 테이블생성(3, true);
        OrderTable 그룹있는_테이블2 = 테이블생성(3, true);
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(Arrays.asList(그룹있는_테이블1, 그룹있는_테이블2));

        tableGroupService.ungroup(1L);

        assertThat(그룹있는_테이블1.getTableGroup()).isNull();
        assertThat(그룹있는_테이블2.getTableGroup()).isNull();
    }

    @Test
    void 주문상태가_요리중이거나_식사중이면_테이블그룹을_삭제할_수_없다() {
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);

        doThrow(IllegalArgumentException.class).when(orderValidator).validateOrderTableStatus(any());

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
