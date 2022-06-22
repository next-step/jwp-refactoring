package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroupEntity;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.request.OrderTableRequest;
import kitchenpos.table.domain.request.TableGroupRequest;
import kitchenpos.table.domain.response.OrderTableResponse;
import kitchenpos.table.domain.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블_2_request;
    private TableGroupRequest 테이블_그룹_request;

    private OrderTableEntity 주문_테이블;
    private OrderTableEntity 주문_테이블2;

    @BeforeEach
    void setUp() {
        주문_테이블_request = new OrderTableRequest(1L, null, 3, true);
        주문_테이블_2_request = new OrderTableRequest(2L, null, 5, true);
        테이블_그룹_request = new TableGroupRequest(1L, Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId()));

        주문_테이블 = OrderTableEntity.of(1L, null, 3, true);
        주문_테이블2 = OrderTableEntity.of(2L, null, 5, true);
    }

    @DisplayName("주문 테이블을 단체지정하면 정상적으로 단체지정 되어야한다")
    @Test
    void create_test() {
        // given
        List<OrderTableEntity> 주문_테이블_목록 = Arrays.asList(주문_테이블, 주문_테이블2);
        TableGroupEntity 테이블_그룹 = TableGroupEntity.of(1L, 주문_테이블_목록);

        // given
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId())))
            .thenReturn(주문_테이블_목록);
        when(tableGroupRepository.save(any()))
            .thenReturn(테이블_그룹);

        // when
        TableGroupResponse result = tableGroupService.create(테이블_그룹_request);

        // then
        List<OrderTableResponse> 그룹_ID_있는_테이블 = result.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertAll(
            () -> assertThat(result.getCreatedDate()).isNotNull(),
            () -> assertThat(그룹_ID_있는_테이블).hasSize(2),
            () -> assertThat(result.getOrderTables().size()).isEqualTo(
                테이블_그룹_request.getOrderTableIds().size())
        );
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        테이블_그룹_request = new TableGroupRequest(null, Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        테이블_그룹_request = new TableGroupRequest(null, Collections.singletonList(주문_테이블_request.getId()));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 모두 존재하지 않으면 예외갑 발생한다")
    @Test
    void create_exception_test3() {
        // given
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문_테이블_request.getId(), 주문_테이블_2_request.getId())))
            .thenReturn(Collections.singletonList(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 비어있지 않거나 "
        + "이미 테이블 그룹이 존재하면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        주문_테이블 = OrderTableEntity.of(1L, null, 3, false);
        주문_테이블2 = OrderTableEntity.of(2L, 3L, 3, true);
        when(orderTableRepository.findAllByIdIn(Arrays.asList(주문_테이블.getId(), 주문_테이블2.getId())))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블2));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹_request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정을 해제하면 정상적으로 해제되어야 한다")
    @Test
    void ungroup_test() {
        // given
        Long 테이블_그룹_id = 3L;

        when(orderTableRepository.findAllByTableGroupId(테이블_그룹_id))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블2));

        // when
        tableGroupService.ungroup(테이블_그룹_id);

        // then
        assertNull(주문_테이블.getTableGroupId());
        assertNull(주문_테이블2.getTableGroupId());
    }

    @DisplayName("단체지정을 해제시 테이블의 주문이 요리중, 식사중인 상태가 있으면 예외가 발생한다")
    @Test
    void ungroup_exception_test() {
        // given
        Long 테이블_그룹_id = 3L;

        when(orderTableRepository.findAllByTableGroupId(테이블_그룹_id))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(주문_테이블.getId(), 주문_테이블2.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(테이블_그룹_id);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
