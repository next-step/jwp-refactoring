package kitchenpos.tablegroup.application;

import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("TableGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(tableGroup.getId())
        );
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        Order order1 = TestOrderFactory.createCompleteOrderWith(orderTable1);
        Order order2 = TestOrderFactory.createCompleteOrderWith(orderTable2);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
