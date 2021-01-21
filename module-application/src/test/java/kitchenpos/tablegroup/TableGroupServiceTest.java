package kitchenpos.tablegroup;

import kitchenpos.common.BaseTest;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 그룹 비즈니스 로직을 처리하는 서비스 테스트")
class TableGroupServiceTest extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));

        tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTableIds(Arrays.asList(3L, 4L, 5L));
    }

    @DisplayName("테이블 그룹(단체 지정)을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(savedTableGroup.getId());
        assertThat(savedTableGroup.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("주문 테이블이 1개 이하인 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블이_1개_이하인_경우_테이블_그룹_생성() {
        tableGroupRequest.setOrderTableIds(Collections.singletonList(1L));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문 테이블이 포함된 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블이_존재하는_경우_테이블_그룹_생성() {
        tableGroupRequest.setOrderTableIds(Arrays.asList(1L, 182L));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 포함된 경우 테이블 그룹을 생성할 수 없다.")
    @Test
    void 다른_테이블_그룹_포함된_경우_테이블_그룹_생성() {
        tableGroupRequest.setOrderTableIds(Arrays.asList(1L, 2L));
        tableGroupService.create(tableGroupRequest);

        final TableGroupRequest secondTableGroupRequest = new TableGroupRequest();
        secondTableGroupRequest.setOrderTableIds(Arrays.asList(1L, 3L));

        assertThatThrownBy(() -> tableGroupService.create(secondTableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        tableGroupRequest.setOrderTableIds(Arrays.asList(1L, 2L));
        final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(savedTableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(savedTableGroup.getId());
        assertThat(savedTableGroup.getOrderTables().get(1).getTableGroupId()).isEqualTo(savedTableGroup.getId());

        tableGroupService.ungroup(savedTableGroup.getId());

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L));

        assertThat(orderTables.get(0).getTableGroupId()).isNull();
        assertThat(orderTables.get(1).getTableGroupId()).isNull();

        assertThatThrownBy(() -> tableGroupRepository.findById(savedTableGroup.getId())
            .orElseThrow(NotFoundException::new))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("테이블 그룹의 주문 상태가 조리인 경우 해제할 수 없다.")
    @Test
    void 주문_상태가_조리인_경우_테이블_그룹_해제() {
        final TableGroupResponse savedTableGroup = 주문_상태가_조리_또는_식사인_주문_생성(OrderStatus.COOKING);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroupResponse 주문_상태가_조리_또는_식사인_주문_생성(final OrderStatus orderStatus) {
        final Order cookingOrder = Order.of(1L);
        cookingOrder.changeOrderStatus(orderStatus);
        orderRepository.save(cookingOrder);

        tableGroupRequest.setOrderTableIds(Arrays.asList(1L, 2L));
        return tableGroupService.create(tableGroupRequest);
    }

    @DisplayName("테이블 그룹의 주문 상태가 식사인 경우 해제할 수 없다.")
    @Test
    void 주문_상태가_식사인_경우_테이블_그룹_해제() {
        final TableGroupResponse savedTableGroup = 주문_상태가_조리_또는_식사인_주문_생성(OrderStatus.MEAL);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
