package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
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
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(orderTables);
        given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).containsAll(orderTables);
    }

    @Test
    void 주문_테이블이_비어있다면_테이블_그룹을_등록할_수_있다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_2개_이하일_경우_테이블_그룹을_등록할_수_없다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(firstTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록되지_않은_경우_테이블_그룹을_등록할_수_없다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_이용중인_경우_테이블_그룹을_등록할_수_없다() {
        firstTable.setEmpty(false);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_이미_테이블_그룹으로_등록되어_있는_경우_테이블_그룹을_등록할_수_없다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        firstTable.setTableGroup(tableGroup);
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.emptyList());
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);

        tableGroupService.ungroup(1L);

        assertThat(firstTable.getTableGroup()).isNull();
        assertThat(secondTable.getTableGroup()).isNull();
    }

    @Test
    void 주문_상태가_조리_또는_식사중이면_테이블_그룹을_해제할_수_없다() {
        List<OrderStatus> orderStatus = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), orderStatus)).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
