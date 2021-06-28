package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    private Long tableGroupId = 1L;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private List<Long> orderTableIds;

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        tableGroupId = 1L;

        orderTable1 = new OrderTable(1L, tableGroupId, 1, true);
        orderTable2 = new OrderTable(2L, tableGroupId, 2, true);

        orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        orderTables = Arrays.asList(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문 테이블이 비어있거나, 1개밖에 없을경우 IllegalArugmentException이 발생한다.")
    void 등록을_원하는_주문_테이블이_비어있거나_1개밖에_없을경우_IllegalArgumentException이_발생한다() {
        TableGroup nullTableGroup = new TableGroup(1L, LocalDateTime.now(), null);
        TableGroup emptyTableGroup = new TableGroup(2L, LocalDateTime.now(), Arrays.asList());
        TableGroup onlyOneTableGroup = new TableGroup(3L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(1L, 3L, 0, true)));

        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(nullTableGroup));
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(emptyTableGroup));
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(onlyOneTableGroup));
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문 테이블과 등록된 주문 테이블의 개수가 틀릴경우 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주문_테이블과_등록된_주문_테이블의_개수가_틀릴경우_IllegalArgumentException이_발생한다() {
        // given
        Long tableGroupId = 1L;


        TableGroup tableGroup = new TableGroup(tableGroupId, LocalDateTime.now(), orderTables);

        // when
        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(Arrays.asList(orderTable1));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByIdIn(orderTableIds);
    }

    @Test
    @DisplayName("create - 등록된 주문 테이블이 빈테이블이 아닐경우 IllegalArgumentException이 발생한다.")
    void 등록된_주문_테이블이_빈테이블이_아닐경우_IllegalArgumentException이_발생한다() {
        // given
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        TableGroup tableGroup = new TableGroup(tableGroupId, LocalDateTime.now(), orderTables);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByIdIn(orderTableIds);
    }

    @Test
    @DisplayName("create - 이미 단체 지정이 되어있을 경우 IllegalArgumentException이 발생한다.")
    void 이미_단체_지정이_되어있을_경우_IllegalArgumentException이_발생한다() {
        // given
        given(orderTableDao.findAllByIdIn(orderTableIds))
                .willReturn(orderTables);

        TableGroup tableGroup = new TableGroup(tableGroupId, LocalDateTime.now(), orderTables);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByIdIn(orderTableIds);

    }

    @Test
    @DisplayName("create - 정상적인 단체지정 등록")
    void 정상적인_단체지정_등록() {
        // given

        OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        TableGroup tableGroup = new TableGroup(tableGroupId, LocalDateTime.now(), orderTables);

        // when
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getOrderTables())
                .map(item -> item.isEmpty())
                .containsOnly(false);

        assertThat(savedTableGroup.getOrderTables())
                .map(item -> item.getTableGroupId())
                .containsOnly(tableGroupId);

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByIdIn(orderTableIds);

        verify(orderTableDao, VerificationModeFactory.times(1))
                .save(orderTable1);
        verify(orderTableDao, VerificationModeFactory.times(1))
                .save(orderTable2);
    }

    @Test
    @DisplayName("unGroup - 주문 테이블들의 고유 아이디를를 조회했을 때 주문 상태가 조리 이거나, 식사 일경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블들의_고유_아이디를_조회했을_때_주문_상태가_조리_이거나_식사_일경우_IllegalArgumentException이_발생한다() {
        // given
        Long tableGroupId = 1L;

        List<String> bannedStatus = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);

        // when
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, bannedStatus))
                .thenReturn(true);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(tableGroupId));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByTableGroupId(tableGroupId);

        verify(orderDao, VerificationModeFactory.times(1))
                .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, bannedStatus);
    }

    @Test
    @DisplayName("unGroup - 정상적인 단체지정 해제")
    void 정상적인_단체지정_해제() {
        // given
        Long tableGroupId = 1L;
        List<String> bannedStatus = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, bannedStatus))
                .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAllByTableGroupId(tableGroupId);

        verify(orderDao, VerificationModeFactory.times(1))
                .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, bannedStatus);

        verify(orderTableDao, VerificationModeFactory.times(1))
                .save(orderTable1);
        verify(orderTableDao, VerificationModeFactory.times(1))
                .save(orderTable2);
    }
}