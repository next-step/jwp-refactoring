package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.*;
import static kitchenpos.application.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        OrderTableFixture.init();
        TableGroupFixture.init();

        when(orderTableDao.findAllByIdIn(Arrays.asList(주문1_단체테이블.getId(), 주문2_단체테이블.getId())))
            .thenReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(단체_테이블그룹);
    }

    @DisplayName("TableGroup 을 등록한다.")
    @Test
    void create1() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).isEqualTo(단체_테이블그룹);
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 0개면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 1개면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(주문1_단체테이블));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        tableGroup.setCreatedDate(LocalDateTime.now());

        when(orderTableDao.findAllByIdIn(Arrays.asList(주문1_단체테이블.getId(), 주문2_단체테이블.getId())))
            .thenReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(손님_10명_개인테이블, 주문2_단체테이블));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 이미 그룹에 속해있으면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        tableGroup.setCreatedDate(LocalDateTime.now());

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

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
        when(orderTableDao.findAllByTableGroupId(단체_테이블그룹.getId()))
            .thenReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        // when
        tableGroupService.ungroup(단체_테이블그룹.getId());

        // then
        verify(orderTableDao, times(2)).save(any(OrderTable.class));
        assertThat(주문1_단체테이블.getTableGroupId()).isNull();
        assertThat(주문2_단체테이블.getTableGroupId()).isNull();
    }

    @DisplayName("TableGroup 해제 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void ungroup2() {
        // given
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());
        주문2_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);
        when(orderTableDao.findAllByTableGroupId(단체_테이블그룹.getId()))
            .thenReturn(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체_테이블그룹.getId()));
    }
}