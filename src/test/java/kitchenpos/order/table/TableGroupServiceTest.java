package kitchenpos.order.table;

import kitchenpos.order.domain.Order;
import kitchenpos.order.table.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.table.domain.OrderTable;
import kitchenpos.order.table.domain.OrderTableRepository;
import kitchenpos.order.table.domain.TableGroup;
import kitchenpos.order.table.domain.TableGroupRepository;
import kitchenpos.order.table.dto.OrderTableRequest;
import kitchenpos.order.table.dto.OrderTableResponse;
import kitchenpos.order.table.dto.TableGroupRequest;
import kitchenpos.order.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;


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

        when(orderTableRepository.findAllById(anyList())).thenReturn(orderTables);
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        //when
        TableGroupResponse savedTableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(savedTableGroupResponse).isNotNull();
        assertThat(savedTableGroupResponse.getId()).isGreaterThan(0L);
        assertThat(savedTableGroupResponse.getOrderTables().size()).isEqualTo(2);
        assertThat(savedTableGroupResponse.getOrderTables().stream()
                .map(OrderTableResponse::isEmpty))
                .contains(false, false);

        assertThat(savedTableGroupResponse.getOrderTables().stream()
                .map(OrderTableResponse::getNumberOfGuests))
                .contains(10, 4);
    }

    @DisplayName("지정된 단체 테이블 해제하기")
    @Test
    void unGroupingTable() {

        //given
        OrderTable orderTableA = OrderTable.create(10, true);
        Order orderA = new Order();
        orderA.completion();;
        orderTableA.order(orderA);

        OrderTable orderTableB = OrderTable.create(4, true);
        Order orderB = new Order();
        orderB.completion();;
        orderTableB.order(orderB);

        TableGroup tableGroup = new TableGroup();
        ReflectionTestUtils.setField(tableGroup, "id", 1L);
        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);

        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(tableGroup));

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(orderTableA.getTableGroup()).isNull();
        assertThat(orderTableB.getTableGroup()).isNull();
    }

}
