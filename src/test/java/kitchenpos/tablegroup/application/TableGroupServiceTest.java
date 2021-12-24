package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
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
            TableGroup expectedTableGroup = new TableGroup(1L, LocalDateTime.now());

            given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expectedTableGroup);

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
    }

}
