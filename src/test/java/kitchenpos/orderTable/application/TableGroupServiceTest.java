package kitchenpos.orderTable.application;

import static kitchenpos.orderTable.domain.TableGroup.ORDER_TABLE_REQUEST_MIN;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import kitchenpos.order.application.OrderService;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.domain.OrderTables;
import kitchenpos.orderTable.domain.TableGroup;
import kitchenpos.orderTable.domain.TableGroupRepository;
import kitchenpos.orderTable.dto.TableGroupRequest;
import kitchenpos.orderTable.dto.TableGroupResponse;
import kitchenpos.orderTable.validator.TableGroupValidator;
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
    private OrderService orderService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableGroupValidator tableGroupValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 치킨주문테이블;
    private OrderTable 단체지정_치킨주문테이블;
    private OrderTable 피자주문테이블;
    private OrderTable 단체지정_피자주문테이블;

    @BeforeEach
    void setUp() {
        치킨주문테이블 = createOrderTable(1L, 2, true);
        피자주문테이블 = createOrderTable(2L, 3, true);
        단체지정_치킨주문테이블 = createOrderTable(1L, 2, true);
        단체지정_피자주문테이블 = createOrderTable(2L, 3, true);
    }

    @DisplayName("단체지정 생성 테스트")
    @Test
    void create() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        TableGroup 단체지정 = createTableGroup(OrderTables.from(Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블)),
                Lists.newArrayList(단체지정_치킨주문테이블.id(), 단체지정_피자주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        assertThat(tableGroupResponse.getId()).isEqualTo(단체지정.id());
    }

    @DisplayName("단체지정 생성시 주문테이블이 2 미만인 경우 테스트")
    @Test
    void createWithOrderTableSizeUnderTwo() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블이 null 인 경우 테스트")
    @Test
    void createWithOrderTableNull() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("단체지정 생성시 단체지정의 주문테이블 수와 등록된 주문테이블에서 조회된 단체지정의 주문테이블 수가 일치하지 않는 경우 테스트")
    @Test
    void createWithNotEqualOrderTableSize() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블이 비어있지 않는 경우 테스트")
    @Test
    void createWithOrderTableNotEmpty() {
        피자주문테이블 = createOrderTable(2L, 3, false);
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("주문테이블이 비어있어야 합니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블의 테이블 그룹이 이미 있는 경우 테스트")
    @Test
    void createWithOrderTableAlreadyContainTableGroup() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        createTableGroup(OrderTables.from(Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블)),
                Lists.newArrayList(단체지정_치킨주문테이블.id(), 단체지정_피자주문테이블.id()));
        given(orderTableRepository.findAllByIdIn(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()))).willReturn(
                Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("단체지정이 없어야 합니다.");
    }

    @DisplayName("단체지정 해제 테스트")
    @Test
    void ungroup() {
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        tableGroupService.ungroup(1L);
        verify(tableGroupValidator).validateComplete(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        verify(orderTableRepository).save(치킨주문테이블);
        verify(orderTableRepository).save(피자주문테이블);
    }
}
