package kitchenpos.order.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
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
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 치킨주문테이블;
    private OrderTable 피자주문테이블;
    private OrderTable 단체지정_치킨주문테이블;
    private OrderTable 단체지정_피자주문테이블;
    private TableGroup 단체지정;

    @BeforeEach
    void setUp() {
        치킨주문테이블 = createOrderTable(1L, null, 2, true);
        피자주문테이블 = createOrderTable(2L, null, 3, true);
        단체지정 = createTableGroup(1L, Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        단체지정_치킨주문테이블 = createOrderTable(1L, 단체지정, 2, true);
        단체지정_피자주문테이블 = createOrderTable(2L, 단체지정, 3, true);
    }

    @DisplayName("단체지정 생성 테스트")
    @Test
    void create() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        given(tableGroupRepository.save(단체지정)).willReturn(단체지정);
        given(orderTableRepository.save(단체지정_치킨주문테이블)).willReturn(단체지정_치킨주문테이블);
        given(orderTableRepository.save(단체지정_피자주문테이블)).willReturn(단체지정_피자주문테이블);
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        assertAll(
                () -> assertThat(tableGroupResponse.getOrderTables()).containsExactlyElementsOf(
                        Lists.newArrayList(OrderTableResponse.from(치킨주문테이블), OrderTableResponse.from(피자주문테이블))),
                () -> assertThat(tableGroupResponse.getId()).isEqualTo(단체지정.id())
        );
    }

    @DisplayName("단체지정 생성시 주문테이블이 2 미만인 경우 테스트")
    @Test
    void createWithOrderTableSizeUnderTwo() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체지정 생성시 주문테이블이 null 인 경우 테스트")
    @Test
    void createWithOrderTableNull() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체지정 생성시 단체지정의 주문테이블 수와 등록된 주문테이블에서 조회된 단체지정의 주문테이블 수가 일치하지 않는 경우 테스트")
    @Test
    void createWithNotEqualOrderTableSize() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체지정 생성시 주문테이블이 비어있지 않는 경우 테스트")
    @Test
    void createWithOrderTableNotEmpty() {
        피자주문테이블 = createOrderTable(2L, null, 3, false);
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체지정 생성시 주문테이블이 테이블 그룹이 이미 있는 경우 테스트")
    @Test
    void createWithOrderTableAlreadyContainTableGroup() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 단체지정_피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 단체지정_피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체지정 해제 테스트")
    @Test
    void ungroup() {
        given(orderTableRepository.findAllByTableGroupId(단체지정.id())).willReturn(
                Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(Lists.newArrayList(치킨주문테이블, 피자주문테이블),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        tableGroupService.ungroup(단체지정.id());
        verify(orderTableRepository).save(치킨주문테이블);
        verify(orderTableRepository).save(피자주문테이블);
    }

    @DisplayName("단체지정 해제시 단체지정의 주문테이블들 중 주문상태가 조리, 식사인 것이 있는 경우 테스트")
    @Test
    void ungroupWithCookingOrMealOrderStatus() {
        given(orderTableRepository.findAllByTableGroupId(단체지정.id())).willReturn(
                Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(Lists.newArrayList(치킨주문테이블, 피자주문테이블),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(단체지정.id()));
    }
}
