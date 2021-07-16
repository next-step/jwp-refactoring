package kitchenpos.table.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.OrderTableRepository;
import kitchenpos.table.domain.entity.TableGroup;
import kitchenpos.table.domain.entity.TableGroupRepository;
import kitchenpos.table.domain.value.NumberOfGuests;
import kitchenpos.table.domain.value.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
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
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    OrderTable 오더테이블_테이블1;
    OrderTableRequest 오더테이블_테이블1_리퀘스트;
    OrderTable 오더테이블_테이블2;
    OrderTableRequest 오더테이블_테이블2_리퀘스트;

    TableGroup 테이블그룹_테이블1_테이블2;
    TableGroupRequest 테이블그룹_테이블1_테이블2_리퀘스트;
    TableGroupRequest 테이블그룹_테이블1_리퀘스트;

    @BeforeEach
    void setUp() {
        오더테이블_테이블1 = new OrderTable(1L, null, NumberOfGuests.of(44), true);
        오더테이블_테이블1_리퀘스트 = new OrderTableRequest(1L, null, 3, true);
        오더테이블_테이블2 = new OrderTable(2L, null, NumberOfGuests.of(33), true);
        오더테이블_테이블2_리퀘스트 = new OrderTableRequest(2L, null, 3, true);

        테이블그룹_테이블1_테이블2 = new TableGroup(1L,
            new OrderTables(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2)));
        테이블그룹_테이블1_테이블2_리퀘스트 = new TableGroupRequest(
            Arrays.asList(오더테이블_테이블1_리퀘스트, 오더테이블_테이블2_리퀘스트));

        테이블그룹_테이블1_리퀘스트 = new TableGroupRequest(Arrays.asList(오더테이블_테이블1_리퀘스트));
    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() {
        //given
        when(orderTableRepository.findAllById(anyList()))
            .thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹_테이블1_테이블2);

        //when
        TableGroupResponse createdTableGroup = tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트);

        //then
        assertThat(createdTableGroup.getId()).isEqualTo(테이블그룹_테이블1_테이블2.getId());
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one() {
        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables() {
        //given
        TableGroupRequest 테이블그룹_테이블1_테이블1_리퀘스트 = new TableGroupRequest(
            Arrays.asList(오더테이블_테이블1_리퀘스트, 오더테이블_테이블1_리퀘스트));

        when(
            orderTableRepository.findAllById(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블1.getId())))
            .thenReturn(Arrays.asList(오더테이블_테이블1));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블1_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty() {
        //given
        오더테이블_테이블2_리퀘스트 = new OrderTableRequest(2L, null, 3, false);
        오더테이블_테이블2 = new OrderTable(2L, NumberOfGuests.of(4), false);
        when(
            orderTableRepository.findAllById(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블2.getId())))
            .thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        //when(tableGroupRepository.save(any())).thenReturn(테이블그룹_테이블1_테이블2);

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_has_table_group() {
        //given
        TableGroup tableGroup = new TableGroup();
        오더테이블_테이블2 = new OrderTable(2L, tableGroup, NumberOfGuests.of(4), true);

        when(orderTableRepository.findAllById(anyList()))
            .thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() {
        //given
        when(tableGroupRepository.findById(테이블그룹_테이블1_테이블2.getId()))
            .thenReturn(Optional.of(테이블그룹_테이블1_테이블2));
        when(orderTableRepository.findAllByTableGroupId(anyLong()))
            .thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .thenReturn(false);

        //when
        tableGroupService.ungroup(테이블그룹_테이블1_테이블2.getId());

        //then
        assertAll(() -> {
            assertThat(오더테이블_테이블1.getTableGroup()).isNull();
            assertThat(오더테이블_테이블2.getTableGroup()).isNull();
        });
    }

    @Test
    @DisplayName("조리중이거나 식사중일 경우 해체에 실패한다.")
    void ungroup_with_exception_when_orderStatus_is_cooking_or_meal() {
        //given
        when(tableGroupRepository.findById(테이블그룹_테이블1_테이블2.getId()))
            .thenReturn(Optional.of(테이블그룹_테이블1_테이블2));
        when(orderTableRepository.findAllByTableGroupId(anyLong()))
            .thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        //when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹_테이블1_테이블2.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}