package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static kitchenpos.fixture.OrderTableTestFixture.*;
import static kitchenpos.fixture.TableGroupTestFixture.테이블그룹;
import static kitchenpos.fixture.TableGroupTestFixture.테이블그룹요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    TableGroupRequest 단체1_요청;
    private TableGroup 단체1;

    @BeforeEach
    void setUp() {
        주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블(1L, null, 10, true));
        주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블(2L, null, 20, true));
        단체1_요청 = 테이블그룹요청(주문정보요청목록(Arrays.asList(주문테이블1, 주문테이블2)));
        단체1 = TableGroup.of(OrderTables.from(mapToEntityForNoGroup(단체1_요청.getOrderTables())));
    }

    @DisplayName("주문 테이블들의 단체 지정을 성공한다.")
    @Test
    void create() {
        // given
        mapToEntityForNoGroup(단체1_요청.getOrderTables()).forEach(OrderTable::unGroup);
        mapToEntityForNoGroup(단체1_요청.getOrderTables()).forEach(table -> table.changeEmpty(true));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupRepository.save(any())).thenReturn(단체1);

        // when
        TableGroupResponse saveTableGroup = tableGroupService.create(단체1_요청);

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroup().getId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블2.getTableGroup().getId()).isEqualTo(saveTableGroup.getId())
        );
    }

    @DisplayName("단체 지정을 할 때, 주문 테이블이 2개 미만이면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        TableGroupRequest tableGroupRequest = 테이블그룹요청(주문정보요청목록(singletonList(주문테이블1)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체 지정을 할 때, 테이블이 비어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
        OrderTable orderTable = 그룹_없는_주문테이블_생성(주문테이블(1L, null, 10, false));
        TableGroupRequest tableGroup = 테이블그룹요청(주문정보요청목록(Arrays.asList(orderTable, 주문테이블1)));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블1.getId()))).thenReturn(
                Arrays.asList(orderTable, 주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 할 때, 다른 단체에 포함된 주문 테이블이 있다면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        OrderTable orderTable = 그룹_있는_주문테이블_생성(주문테이블(1L, 3L, 10, true));
        setMenuGroup(테이블그룹(), orderTable);
        TableGroupRequest tableGroup = 테이블그룹요청(주문정보요청목록(Arrays.asList(orderTable, 주문테이블1)));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블1.getId()))).thenReturn(
                Arrays.asList(orderTable, 주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 해제를 성공한다.")
    @Test
    void ungroup() {
        // given
        when(orderTableRepository.findAllByTableGroupId(단체1.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(false);

        // when
        tableGroupService.ungroup(단체1.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroup()).isNull(),
                () -> assertThat(주문테이블2.getTableGroup()).isNull()
        );
    }

    @DisplayName("단체 지정을 해제할 때, 주문 테이블의 상태가 조리중이거나 식사중이면 IllegalArgumentException을 반환한다.")
    @Test
    void ungroupWithException1() {
        // given
        when(orderTableRepository.findAllByTableGroupId(단체1.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체1.getId()));
    }
}
