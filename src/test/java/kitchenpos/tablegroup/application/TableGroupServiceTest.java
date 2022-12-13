package kitchenpos.tablegroup.application;

import static kitchenpos.table.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.tablegroup.domain.TableGroupTest.단체_생성;
import static kitchenpos.tablegroup.dto.TableGroupRequestTest.단체_지정_생성_요청_객체_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
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

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private TableGroup 단체;

    @BeforeEach
    public void setUp() {
        주문_테이블_1 = 주문_테이블_생성(1L, null, 0, true);
        주문_테이블_2 = 주문_테이블_생성(2L, null, 0, true);
        단체 = 단체_생성(1L, Arrays.asList(주문_테이블_1, 주문_테이블_2));
    }


    @Test
    @DisplayName("단체 지정 등록")
    void create() {
        // given
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(단체);
        주문_테이블_1.setTableGroup(null);
        주문_테이블_2.setTableGroup(null);
        TableGroupRequest 단체_지정_생성_요청_객체 = 단체_지정_생성_요청_객체_생성(주문_테이블_1.tableGroupId(), 주문_테이블_2.tableGroupId());

        // when
        TableGroupResponse 단체_지정_생성_응답_객체 = tableGroupService.create(단체_지정_생성_요청_객체);

        // then
        assertThat(단체_지정_생성_응답_객체.getId()).isEqualTo(단체.getId());
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        // given
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertThat(주문_테이블_1.tableGroupId()).isNull();
        assertThat(주문_테이블_2.tableGroupId()).isNull();
    }
}