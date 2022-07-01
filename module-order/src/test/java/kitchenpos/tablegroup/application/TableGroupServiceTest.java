package kitchenpos.tablegroup.application;

import static kitchenpos.tablegroup.domain.TableGroup.ORDER_TABLE_REQUEST_MIN;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.event.ReserveEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체지정;
    private OrderTable 치킨주문테이블;
    private OrderTable 피자주문테이블;

    @BeforeEach
    void setUp() {
        단체지정 = createTableGroup(1L);
        치킨주문테이블 = createOrderTable(1L, 2, true);
        피자주문테이블 = createOrderTable(2L, 3, true);
    }

    @DisplayName("단체지정 생성 테스트")
    @Test
    void create() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        verify(applicationEventPublisher).publishEvent(any(ReserveEvent.class));
        assertThat(tableGroupResponse.getId()).isEqualTo(단체지정.id());
    }

    @DisplayName("단체지정 생성시 주문테이블이 2 미만인 경우 테스트")
    @Test
    void createWithOrderTableSizeUnderTwo() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        willThrow(new IllegalArgumentException(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.")).given(
                applicationEventPublisher).publishEvent(any(ReserveEvent.class));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블이 null 인 경우 테스트")
    @Test
    void createWithOrderTableNull() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(null);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        willThrow(new IllegalArgumentException(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.")).given(
                applicationEventPublisher).publishEvent(any(ReserveEvent.class));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("단체지정 생성시 단체지정의 주문테이블 수와 등록된 주문테이블에서 조회된 단체지정의 주문테이블 수가 일치하지 않는 경우 테스트")
    @Test
    void createWithNotEqualOrderTableSize() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        willThrow(new IllegalArgumentException("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.")).given(applicationEventPublisher)
                .publishEvent(any(ReserveEvent.class));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블이 비어있지 않는 경우 테스트")
    @Test
    void createWithOrderTableNotEmpty() {
        피자주문테이블 = createOrderTable(2L, 3, false);
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        willThrow(new IllegalArgumentException("주문테이블이 비어있어야 합니다.")).given(applicationEventPublisher)
                .publishEvent(any(ReserveEvent.class));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("주문테이블이 비어있어야 합니다.");
    }

    @DisplayName("단체지정 생성시 주문테이블의 테이블 그룹이 이미 있는 경우 테스트")
    @Test
    void createWithOrderTableAlreadyContainTableGroup() {
        TableGroupRequest tableGroupRequest = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);
        willThrow(new IllegalArgumentException("단체지정이 없어야 합니다.")).given(applicationEventPublisher)
                .publishEvent(any(ReserveEvent.class));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("단체지정이 없어야 합니다.");
    }

    @DisplayName("단체지정 해제 테스트")
    @Test
    void ungroup() {
        given(orderTableRepository.findAllByTableGroupId(단체지정.id())).willReturn(Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        tableGroupService.ungroup(단체지정.id());
        tableGroupValidator.validateComplete(Lists.newArrayList(치킨주문테이블.id(), 피자주문테이블.id()));
    }
}
