package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Nested
    class CreateTableGroupTest {
        @DisplayName("단체 지정을 생성한다")
        @Test
        void testCreate() {
            // given
            OrderTableRequest firstOrderTableRequest = new OrderTableRequest(1L, 4, true);
            OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2L, 4, true);
            List<OrderTableRequest> orderTableRequests = Arrays.asList(firstOrderTableRequest, secondOrderTableRequest);
            TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

            OrderTable firstOrderTable = new OrderTable(firstOrderTableRequest.getId(), null, firstOrderTableRequest.getNumberOfGuests(), firstOrderTableRequest.isEmpty());
            OrderTable secondOrderTable = new OrderTable(secondOrderTableRequest.getId(), null, secondOrderTableRequest.getNumberOfGuests(), secondOrderTableRequest.isEmpty());
            List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
            TableGroup expectedTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

            given(orderTableDao.findAllByIdIn(any(List.class))).willReturn(orderTables);
            given(tableGroupDao.save(any(TableGroup.class))).willReturn(expectedTableGroup);
            given(orderTableDao.save(any(OrderTable.class))).willReturn(firstOrderTable, secondOrderTable);

            // when
            TableGroupResponse tableGroup = tableGroupService.create(tableGroupRequest);

            // then
            assertThat(tableGroup).isEqualTo(TableGroupResponse.of(expectedTableGroup));
        }

        @DisplayName("주문 테이블을 2개 이상 지정해야 한다")
        @Test
        void assignTwoMoreTable() {
            // given
            OrderTableRequest firstOrderTableRequest = new OrderTableRequest(1L, 4, true);
            List<OrderTableRequest> orderTableRequests = Arrays.asList(firstOrderTableRequest);
            TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(tableGroupRequest);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("생성된 주문 테이블로 묶어야 한다")
        @Test
        void hasSavedTable() {
            // given
            OrderTableRequest firstOrderTableRequest = new OrderTableRequest(1L, 4, true);
            OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2L, 4, true);
            List<OrderTableRequest> orderTableRequests = Arrays.asList(firstOrderTableRequest, secondOrderTableRequest);
            TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

            given(orderTableDao.findAllByIdIn(any(List.class))).willReturn(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(tableGroupRequest);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블만 단체지정을 할 수 있다")
        @Test
        void mustEmptyTables() {
            // given
            OrderTableRequest firstOrderTableRequest = new OrderTableRequest(1L, 4, true);
            OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2L, 4, true);
            List<OrderTableRequest> orderTableRequests = Arrays.asList(firstOrderTableRequest, secondOrderTableRequest);
            TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

            OrderTable firstOrderTable = new OrderTable(firstOrderTableRequest.getId(), null, firstOrderTableRequest.getNumberOfGuests(), firstOrderTableRequest.isEmpty());
            OrderTable secondOrderTable = new OrderTable(secondOrderTableRequest.getId(), null, secondOrderTableRequest.getNumberOfGuests(), false);
            List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);

            given(orderTableDao.findAllByIdIn(any(List.class))).willReturn(orderTables);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(tableGroupRequest);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 해제")
    @Nested
    class UngroupTableGroupTest {
        @DisplayName("단체 지정을 해제한다")
        @Test
        void testUngroup() {
            // given
            List<OrderTable> orderTables = new ArrayList<>();
            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
            OrderTable firstOrderTable = new OrderTable(1L, tableGroup.getId(), 4, false);
            OrderTable secondOrderTable = new OrderTable(2L, tableGroup.getId(), 4, false);
            orderTables.addAll(Arrays.asList(firstOrderTable, secondOrderTable));

            given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).willReturn(false);
            given(orderTableDao.save(any(OrderTable.class))).willReturn(firstOrderTable, secondOrderTable);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            verify(orderTableDao, times(2)).save(any(OrderTable.class));
        }

        @DisplayName("아직 주문이 생성되지 않아야 한다")
        @Test
        void doNotOrder() {
            // given
            List<OrderTable> orderTables = new ArrayList<>();
            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
            OrderTable firstOrderTable = new OrderTable(1L, tableGroup.getId(), 4, false);
            OrderTable secondOrderTable = new OrderTable(2L, tableGroup.getId(), 4, false);
            orderTables.addAll(Arrays.asList(firstOrderTable, secondOrderTable));

            given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.ungroup(tableGroup.getId());

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
