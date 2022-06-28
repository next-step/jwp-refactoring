package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
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

    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable_1 = TableServiceTest.테이블_생성(1L, 2, true);
        orderTable_2 = TableServiceTest.테이블_생성(2L, 3, true);
        tableGroup = 테이블_그룹_생성(1L, Arrays.asList(orderTable_1, orderTable_2));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(createdTableGroup).isNotNull();
        assertThat(createdTableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블이 1개 이하인 경우, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidOrderTablesSize() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable_1));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 내에 존재하지 않는 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidNotExistsOrderTable() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 내에 빈 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidEmptyOrderTable() {
        //given
        orderTable_1.setEmpty(false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 내에 그룹에 속한 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidOtherGroup() {
        //given
        orderTable_1.setTableGroupId(1L);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        //given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableRepository.save(any())).willReturn(orderTable_1);

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(orderTable_1.getTableGroupId()).isNull();
    }

    @DisplayName("그룹 내 테이블의 주문 상태가 조리나 식사중이면, 그룹을 해제할 수 없다.")
    @Test
    void ungroup_invalidOrderStatus() {
        //given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static TableGroup 테이블_그룹_생성(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
