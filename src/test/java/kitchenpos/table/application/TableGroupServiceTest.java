package kitchenpos.table.application;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.order.domain.OrderLineItemTestFixture.짜장_탕수육_주문_항목;
import static kitchenpos.order.domain.OrderTestFixture.order;
import static kitchenpos.table.domain.OrderTableTestFixture.*;
import static kitchenpos.table.domain.TableGroupTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTablesValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 테이블 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTablesValidator orderTablesValidator;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 테이블 등록시 테이블은 비어있을 수 없다.")
    void createTableGroupByEmptyTable() {
        // given
        TableGroupRequest tableGroupRequest = tableGroupRequest(emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 테이블은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("단체 테이블 등록시 테이블은 2개 이상이어야 한다.")
    void createTableGroupByOrderTableMoreThanOne() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        TableGroupRequest tableGroupRequest = tableGroupRequest(1L);
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 테이블은 2석 이상이어야 합니다.");
    }

    @Test
    @DisplayName("단체 테이블 등록시 모두 등록된 테이블이어야 한다.")
    void createTableGroupByNoneOrderedTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        OrderTable orderTable2 = orderTable(Long.MAX_VALUE, null, 0, true);
        TableGroupRequest tableGroupRequest = tableGroupRequest(orderTable.id(), orderTable2.id());
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다. ID : " + Long.MAX_VALUE);
    }

    @Test
    @DisplayName("단체 테이블 등록시 모두 비어있는 테이블이어야 한다.")
    void createTableGroupByAllIsEmptyTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, false);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroupRequest tableGroupRequest = tableGroupRequest(orderTable.id(), orderTable2.id());
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.findById(orderTable2.id())).willReturn(Optional.of(orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("모두 비어있는 테이블이어야 합니다.");
    }

    @Test
    @DisplayName("단체 테이블 등록시 이미 단체 테이블인 테이블은 등록 할 수 없다.")
    void createTableGroupByAlreadyGroup() {
        // given
        OrderTable orderTable = orderTable(1L, 1L, 0, true);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroupRequest tableGroupRequest = tableGroupRequest(orderTable.id(), orderTable2.id());
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.findById(orderTable2.id())).willReturn(Optional.of(orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }

    @Test
    @DisplayName("단체 테이블로 등록한다.")
    void createTableGroup() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        TableGroupRequest tableGroupRequest = tableGroupRequest(orderTable.id(), orderTable2.id());
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.findById(orderTable2.id())).willReturn(Optional.of(orderTable2));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        // when
        TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(TableGroupResponse.class)
        );
    }

    @Test
    @DisplayName("단체 테이블을 해제한다.")
    void ungroupTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, true);
        OrderTable orderTable2 = orderTable(2L, null, 3, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        Order order = order(1L, orderTable.id(), singletonList(짜장_탕수육_주문_항목));
        Order order2 = order(2L, orderTable2.id(), singletonList(짜장_탕수육_주문_항목));
        order.changeStatus(OrderStatus.COMPLETION);
        order2.changeStatus(OrderStatus.COMPLETION);
        given(tableGroupRepository.findById(tableGroup.id())).willReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(tableGroup.id());

        // then
        assertAll(
                () -> assertThat(orderTable.tableGroupId()).isNull(),
                () -> assertThat(orderTable2.tableGroupId()).isNull()
        );
    }
}
