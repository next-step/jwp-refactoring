package kitchenpos.table.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.factory.fixture.OrderTableFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(1L, true, null);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(orderTable);
        OrderTableResponse response = tableService.create(new OrderTableRequest(1, true));

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
        given(orderTableRepository.findAll()).willReturn(orderTables);

        assertThat(tableService.list()).hasSize(2);
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmpty() {
        Long orderTableId = 1L;
        OrderTableRequest request = new OrderTableRequest(1, true);
        OrderTable orderTable = createOrderTable(1L, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(Boolean.FALSE);

        OrderTableResponse response = tableService.changeEmpty(orderTableId, request);

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블이 존재하지 않는다면, 테이블을 비운다.")
    @Test
    void changeEmpty_notExisted_orderTable() {
        Long orderTableId = 1L;
        OrderTableRequest request = new OrderTableRequest(1, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_NOT_FOUND.getMessage());
    }

    @DisplayName("조리, 식사중인 테이블은 변경할 수 없다.")
    @Test
    void changeEmpty_cooking_meal() {
        Long orderTableId = 1L;
        OrderTableRequest request = new OrderTableRequest(1, true);
        OrderTable orderTable = createOrderTable(1L, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(Boolean.TRUE);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.CAN_NOT_CHANGE_COOKING_AND_MEAL.getMessage());
    }

    @DisplayName("테이블 그룹이 이미 존재한다면, 테이블을 비운다.")
    @Test
    void changeEmpty_nonNull_tableGroupId() {
        Long orderTableId = 1L;
        OrderTableRequest request = new OrderTableRequest(1, false);
        OrderTable orderTable = createOrderTable(orderTableId, false, new TableGroup(1L));

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_GROUPED.getMessage());
    }

    @DisplayName("인원을 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        Long orderTableId = 1L;
        OrderTable orderTable = createOrderTable(0);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, new OrderTableRequest(5, false));

        assertThat(response.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("인원을 1명 미만으로 변경요청 하면  변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalid_numberOfGuests() {
        Long orderTableId = 1L;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new OrderTableRequest(0, false)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_NOT_FOUND.getMessage());
    }

    @DisplayName("주문 테이블이 존재하지 않는다면, 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_notExisted_orderTable() {
        Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new OrderTableRequest(5, false)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_NOT_FOUND.getMessage());
    }

    @DisplayName("주문 테이블이 비어있다면, 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_orderTable() {
        Long orderTableId = 1L;
        OrderTable orderTable = createOrderTable(true, 0);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new OrderTableRequest(5, false)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.TABLE_EMPTY.getMessage());
    }
}
