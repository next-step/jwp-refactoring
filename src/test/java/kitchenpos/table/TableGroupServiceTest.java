package kitchenpos.table;

import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableService tableService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @DisplayName("단체 테이블 지정하기")
    @Test
    void groupingTable() {

        //given
        OrderTableRequest orderTableA = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableA, "id", 1L);
        ReflectionTestUtils.setField(orderTableA, "numberOfGuests", 10);
        ReflectionTestUtils.setField(orderTableA, "empty", true);

        OrderTableRequest orderTableB = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableB, "id", 2L);
        ReflectionTestUtils.setField(orderTableB, "numberOfGuests", 4);
        ReflectionTestUtils.setField(orderTableB, "empty", true);

        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        List<OrderTableRequest> orderTableRequests = Arrays.asList(orderTableA, orderTableB);
        ReflectionTestUtils.setField(tableGroupRequest, "orderTables", orderTableRequests);

        TableGroup tableGroup = tableGroupRequest.toEntity();
        ReflectionTestUtils.setField(tableGroup, "id", 1L);

        List<OrderTable> orderTables = orderTableRequests.stream()
                .map(OrderTableRequest::toEntity)
                .collect(toList());

        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        //when
        TableGroupResponse savedTableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(savedTableGroupResponse).isNotNull();
        assertThat(savedTableGroupResponse.getId()).isGreaterThan(0L);
    }

    @DisplayName("지정된 단체 테이블 해제하기")
    @Test
    void unGroupingTable() {

        //given
        TableGroup tableGroup = TableGroup.setUp();
        ReflectionTestUtils.setField(tableGroup, "id", 1L);

        OrderTable orderTableA = OrderTable.setting(10, false);
        orderTableA.grouping(tableGroup.getId());
        Order orderA = orderTableA.placeOrder();
        orderA.completion();

        OrderTable orderTableB = OrderTable.setting(4, false);
        orderTableB.grouping(tableGroup.getId());
        Order orderB = orderTableB.placeOrder();
        orderB.completion();

        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(tableGroup));

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(orderTableA.getTableGroupId()).isNotNull();
        assertThat(orderTableB.getTableGroupId()).isNotNull();
    }

}
