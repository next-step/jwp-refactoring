package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable firstTable;
    private OrderTable secondTable;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        firstTable = new OrderTable(1L, new NumberOfGuests(0), true);
        secondTable = new OrderTable(2L, new NumberOfGuests(0), true);
        orderTables = Arrays.asList(firstTable, secondTable);
    }

    @Test
    void 테이블_그룹을_등록할_수_있다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTables));
        List<Long> orderTableIds = Arrays.asList(firstTable.getId(), secondTable.getId());

        when(orderTableRepository.findById(firstTable.getId())).thenReturn(Optional.of(firstTable));
        when(orderTableRepository.findById(secondTable.getId())).thenReturn(Optional.of(secondTable));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2)
        );

    }

    @Test
    void 주문_테이블이_비어있다면_테이블_그룹을_등록할_수_없다() {
        List<Long> orderTableIds = Collections.emptyList();

        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
    }

    @Test
    void 주문_테이블이_2개_이하일_경우_테이블_그룹을_등록할_수_없다() {
        List<Long> orderTableIds = Arrays.asList(firstTable.getId());

        when(orderTableRepository.findById(firstTable.getId())).thenReturn(Optional.of(firstTable));
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_TWO_OVER.message());
    }

    @Test
    void 주문_테이블이_등록되지_않은_경우_테이블_그룹을_등록할_수_없다() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(firstTable.getId(), secondTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE.message());
    }

    @Test
    void 주문_테이블이_이미_테이블_그룹으로_등록되어_있는_경우_테이블_그룹을_등록할_수_없다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTables));
        when(orderTableRepository.findById(firstTable.getId())).thenReturn(Optional.of(firstTable));
        when(orderTableRepository.findById(secondTable.getId())).thenReturn(Optional.of(secondTable));

        firstTable.setTableGroup(tableGroup);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(firstTable.getId(), secondTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ALREADY_GROUP.message());
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        Order fistOrder = new Order(firstTable, OrderStatus.COMPLETION, LocalDateTime.now());
        Order secondorder = new Order(secondTable, OrderStatus.COMPLETION, LocalDateTime.now());
        List<Long> orderTableIds = Arrays.asList(firstTable.getId(), secondTable.getId());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(Arrays.asList(firstTable, secondTable)));

        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(orderTableIds)).thenReturn(Arrays.asList(fistOrder, secondorder));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(firstTable.getTableGroup()).isNull(),
                () -> assertThat(secondTable.getTableGroup()).isNull()
        );
    }

    @Test
    void 주문_상태가_조리_또는_식사중이면_테이블_그룹을_해제할_수_없다() {
        // given
        Order firstOrder = new Order(firstTable, OrderStatus.MEAL, LocalDateTime.now());
        Order secondOrder = new Order(secondTable, OrderStatus.COMPLETION, LocalDateTime.now());
        List<Long> orderTableIds = Arrays.asList(firstTable.getId(), secondTable.getId());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(Arrays.asList(firstTable, secondTable)));

        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(orderTableIds)).thenReturn(Arrays.asList(firstOrder, secondOrder));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }
}
