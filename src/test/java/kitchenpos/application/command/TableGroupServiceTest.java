package kitchenpos.application.command;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupCreate;
import kitchenpos.fixture.CleanUp;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableGroupService tableGroupService;

    private Long tableGroupId = 1L;

    private TableGroup tableGroup;

    private List<Long> orderTableIds;

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        this.tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);

        tableGroupId = 1L;

        tableGroup = new TableGroup(tableGroupId, LocalDateTime.now(), new OrderTables(Arrays.asList()));

        orderTableIds = Arrays.asList(미사용중인_테이블.getId(), 미사용중인_테이블2.getId());
        orderTables = Arrays.asList(미사용중인_테이블, 미사용중인_테이블2);
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문 테이블과 등록된 주문 테이블의 개수가 틀릴경우 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주문_테이블과_등록된_주문_테이블의_개수가_틀릴경우_IllegalArgumentException이_발생한다() {
        // given
        TableGroupCreate tableGroup = new TableGroupCreate(orderTableIds);

        // when
        when(orderTableRepository.findAllById(orderTableIds)).thenReturn(Arrays.asList(미사용중인_테이블));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findAllById(orderTableIds);
    }

    @Test
    @DisplayName("create - 등록된 주문 테이블이 빈테이블이 아닐경우 IllegalArgumentException이 발생한다.")
    void 등록된_주문_테이블이_빈테이블이_아닐경우_IllegalArgumentException이_발생한다() {
        // given
        List<Long> orderTableIds = Arrays.asList(미사용중인_테이블.getId(), 사용중인_1명_테이블.getId());
        List<OrderTable> orderTables = Arrays.asList(미사용중인_테이블, 사용중인_1명_테이블);

        TableGroupCreate tableGroup = new TableGroupCreate(orderTableIds);

        given(orderTableRepository.findAllById(orderTableIds)).willReturn(orderTables);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findAllById(orderTableIds);
    }

    @Test
    @DisplayName("create - 이미 단체 지정이 되어있을 경우 IllegalArgumentException이 발생한다.")
    void 이미_단체_지정이_되어있을_경우_IllegalArgumentException이_발생한다() {
        // given
        List<Long> orderTableIds = Arrays.asList(미사용중인_테이블.getId(), 단체만_지정이_되어있고_빈_테이블.getId());
        List<OrderTable> orderTables = Arrays.asList(미사용중인_테이블, 단체만_지정이_되어있고_빈_테이블);

        TableGroupCreate tableGroup = new TableGroupCreate(orderTableIds);

        given(orderTableRepository.findAllById(orderTableIds))
                .willReturn(orderTables);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findAllById(orderTableIds);

    }

    @Test
    @DisplayName("create - 정상적인 단체지정 등록")
    void 정상적인_단체지정_등록() {
        // given
        TableGroupCreate tableGroupCreate = new TableGroupCreate(orderTableIds);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));

        given(orderTableRepository.findAllById(orderTableIds)).willReturn(orderTables);

        // when
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        tableGroupService.create(tableGroupCreate);

        // then
        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findAllById(orderTableIds);

        verify(tableGroupRepository, VerificationModeFactory.times(1))
                .save(any());
    }

    @Test
    @DisplayName("unGroup - 주문 테이블들의 고유 아이디를를 조회했을 때 주문 상태가 조리 이거나, 식사 일경우 IllegalStateException이 발생한다.")
    void 주문_테이블들의_고유_아이디를_조회했을_때_주문_상태가_조리_이거나_식사_일경우_IllegalStateException이_발생한다() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(사용중인_1명_3건_결제완로, 사용중인_1명_1건_결제완료_1건_식사));

        TableGroup tableGroup = new TableGroup(1L, null, orderTables);

        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> tableGroupService.ungroup(1L));

        verify(tableGroupRepository, VerificationModeFactory.times(1))
                .findById(1L);
    }

    @Test
    @DisplayName("unGroup - 정상적인 단체지정 해제")
    void 정상적인_단체지정_해제() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(사용중인_1명_2건_결제완료1, 사용중인_1명_2건_결제완료2));

        TableGroup tableGroup = new TableGroup(1L, null, orderTables);

        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(1L);

        // then
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }

        verify(tableGroupRepository, VerificationModeFactory.times(1))
                .findById(1L);
    }
}