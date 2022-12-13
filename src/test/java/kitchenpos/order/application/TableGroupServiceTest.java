package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;
    private OrderTables 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_테이블 = TableGroup.of(1L);

        주문테이블1 = new OrderTable(4, true);
        주문테이블2 = new OrderTable(4, true);
        주문테이블3 = new OrderTable(2, true);
        주문테이블4 = new OrderTable(2, true);

        ReflectionTestUtils.setField(단체_테이블, "id", 1L);
        ReflectionTestUtils.setField(주문테이블1, "id", 1L);
        ReflectionTestUtils.setField(주문테이블2, "id", 2L);
        ReflectionTestUtils.setField(주문테이블3, "id", 3L);
        ReflectionTestUtils.setField(주문테이블4, "id", 4L);

        주문_테이블_목록 = OrderTables.of(Arrays.asList(주문테이블1, 주문테이블2));
        주문_테이블_목록.group(단체_테이블.getId());
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void 단체_지정() {
        // given
        when(orderTableRepository.findById(주문테이블3.getId())).thenReturn(Optional.of(주문테이블3));
        when(orderTableRepository.findById(주문테이블4.getId())).thenReturn(Optional.of(주문테이블4));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(단체_테이블);

        TableGroupResponse response =
                tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId())));

        assertAll(
                () -> assertThat(단체_테이블.getId()).isEqualTo(response.getId()),
                () -> assertThat(주문_테이블_목록.getOrderTables().size()).isEqualTo(response.getOrderTables().size())
        );
    }

    @DisplayName("테이블을 2개 이상 지정하지 않으면 단체 지정을 할 수 없다.")
    @Test
    void 단일_테이블_단체_지정() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문테이블1));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Collections.singletonList(주문테이블1.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void 등록되지_않은_테이블_단체_지정() {
        // given
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void 빈_테이블이_아닌_테이블_단체_지정() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.findById(주문테이블2.getId())).thenReturn(Optional.of(주문테이블2));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 테이블은 단체 지정을 할 수 없다.")
    @Test
    void 이미_단체_지정된_테이블_단체_지정() {
        ReflectionTestUtils.setField(주문테이블1, "empty", true);
        ReflectionTestUtils.setField(주문테이블2, "empty", true);

        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.findById(주문테이블2.getId())).thenReturn(Optional.of(주문테이블2));
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        when(tableGroupRepository.findById(단체_테이블.getId())).thenReturn(Optional.of(단체_테이블));
        when(orderTableRepository.findAllByTableGroupId(any(Long.class))).thenReturn(주문_테이블_목록.getOrderTables());
        when(orderRepository.findAllByOrderTableIdIn(주문_테이블_목록.getOrderTableIds()))
                .thenReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, OrderStatus.COMPLETION)));

        tableGroupService.ungroup(단체_테이블.getId());

        assertThat(주문_테이블_목록.getOrderTables().stream().allMatch(orderTable -> orderTable.getTableGroupId() == null)).isTrue();
    }

    @DisplayName("등록된 테이블이 아니면 단체 지정을 해제할 수 없다.")
    @Test
    void 등록되지_않은_테이블_단체_지정_해제() {
        when(tableGroupRepository.findById(단체_테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_테이블.getId())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("조리중이거나 식사중인 테이블은 단체 지정을 해제할 수 없다.")
    @Test
    void 조리중_식사중인_테이블_단체_지정_해제() {
        when(tableGroupRepository.findById(any(Long.class))).thenReturn(Optional.of(단체_테이블));
        when(orderTableRepository.findAllByTableGroupId(any(Long.class))).thenReturn(주문_테이블_목록.getOrderTables());
        when(orderRepository.findAllByOrderTableIdIn(주문_테이블_목록.getOrderTableIds()))
                .thenReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, OrderStatus.MEAL)));

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_테이블.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
