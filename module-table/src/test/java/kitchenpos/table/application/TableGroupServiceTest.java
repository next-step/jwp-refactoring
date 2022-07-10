package kitchenpos.table.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private ChangeStateTableValidator changeStateTableValidator;
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("개별 주문 테이블이 2개 미만일 경우 단체석으로 지정할 수 없다.")
    void isLimitOrderTable() {
        //given
        TableGroupRequest tableGroup = new TableGroupRequest(Collections.singletonList(1L));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
            tableGroupService.create(tableGroup)
        );
    }

    @Test
    @DisplayName("단체지정할 주문 테이블이 존재하지 않으면 단체석으로 지정할 수 없다.")
    void isNotExistOrderTable() {
        //given
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(1L, 1L));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(new ArrayList<>());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.create(tableGroup)
        );

    }

    @Test
    @DisplayName("단체지정할 주문 테이블이 없으면(빈 테이블) 단체석으로 지정할 수 없다.")
    void isEmptyOrderTable() {
        //given
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup(1L, OrderTables.from(orderTables));

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.create(tableGroupRequest)
        );
    }

    @Test
    @DisplayName("단체지정할 주문 테이블이 이미 단체석인경우 지정할 수 없다.")
    void isHasGroupOrderTable() {
        //given
        OrderTable orderTable1 = new OrderTable(1L,1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup(1L, OrderTables.from(orderTables));

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableGroupService.create(request)
        );
    }


    @Test
    @DisplayName("단체석이 생성된다.")
    void tableGroupCreate() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup(1L, OrderTables.from(orderTables));

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);


        //when
        final TableGroupResponse saveTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(saveTableGroup.getOrderTables())
                .extracting("tableGroupId")
                .containsExactly(1L, 1L);
    }

    @Test
    @DisplayName("테이블 그룹이 존재하지 않는 경우 개인테이블로 변경되지 않는다")
    void unGroup() {
        //given
        given(tableGroupRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
                tableGroupService.ungroup(1L)
        );
    }

}
