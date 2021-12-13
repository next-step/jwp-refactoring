package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블그룹;
    private OrderTable 주문1_단체테이블;
    private OrderTable 주문2_단체테이블;
    private OrderTable 손님_10명_개인테이블;

    @BeforeEach
    void setUp() {
        주문1_단체테이블 = OrderTableFixtureFactory.create(1L, true);
        주문2_단체테이블 = OrderTableFixtureFactory.create(2L, true);
        손님_10명_개인테이블 = OrderTableFixtureFactory.createWithGuests(3L, 10, true);
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);

        단체_테이블그룹.setOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
    }

    @DisplayName("TableGroup 을 등록한다.")
    @Test
    void create1() {
        // given
        TableGroup tableGroup = TableGroup.from(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문1_단체테이블.getId(), 주문2_단체테이블.getId())))
            .willReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블그룹);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroup);

        // then
        assertThat(tableGroupResponse).isEqualTo(TableGroupResponse.from(단체_테이블그룹));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 0개면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        TableGroup tableGroup = TableGroup.from(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 1개면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        TableGroup tableGroup = TableGroup.from(Arrays.asList(주문1_단체테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        TableGroup tableGroup = TableGroup.from(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        given(orderTableRepository.findAllByIdIn(Arrays.asList(주문1_단체테이블.getId(), 주문2_단체테이블.getId())))
            .willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        TableGroup tableGroup = TableGroup.from(Arrays.asList(손님_10명_개인테이블, 주문2_단체테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 이미 그룹에 속해있으면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        TableGroup tableGroup = TableGroup.from(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 해제한다.")
    @Test
    void ungroup1() {
        // given
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());
        주문2_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableRepository.findAllByTableGroup(단체_테이블그룹.getId())).willReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        // when
        tableGroupService.ungroup(단체_테이블그룹.getId());

        // then
        verify(orderTableRepository, times(2)).save(any(OrderTable.class));
        assertThat(주문1_단체테이블.getTableGroup()).isNull();
        assertThat(주문2_단체테이블.getTableGroup()).isNull();
    }

    @DisplayName("TableGroup 해제 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void ungroup2() {
        // given
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());
        주문2_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
        given(orderTableRepository.findAllByTableGroup(단체_테이블그룹.getId()))
            .willReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체_테이블그룹.getId()));
    }
}