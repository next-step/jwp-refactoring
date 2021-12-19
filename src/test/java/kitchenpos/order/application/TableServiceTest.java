package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void createTable() {
        // given
        OrderTable orderTable = 주문_테이블_생성(0, true);

        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when
        OrderTableResponse savedOrderTable = tableService.create(new OrderTableRequest(
            orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty()));

        // then
        assertEquals(orderTable.getNumberOfGuests().getValue(), savedOrderTable.getNumberOfGuests());
        assertEquals(orderTable.isEmpty(), savedOrderTable.isEmpty());
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void getTables() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
            주문_테이블_생성(0, true));

        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> findOrderTables = tableService.list();

        // then
        assertThat(orderTables)
            .extracting("empty")
            .containsExactlyElementsOf(findOrderTables.stream().map(OrderTableResponse::isEmpty)
                .collect(Collectors.toList()));
    }

    @DisplayName("주문 테이블의 주문 등록 가능 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        OrderTable findOrderTable = 주문_테이블_생성(1L, 0, true);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(findOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);
        given(orderTableRepository.save(findOrderTable)).willReturn(findOrderTable);

        // when
        OrderTableResponse changeTable = tableService.changeEmpty(orderTable.getId(),
            new OrderTableRequest(orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty()));

        // then
        assertFalse(changeTable.isEmpty());
    }

    @DisplayName("주문 테이블이 테이블 그룹에 속해있다면 주문 등록 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmptyExistTableGroup() {
        // given
        OrderTable orderTable = 주문_테이블_생성(1L, 1L, 0, true);
        Long orderTableId = orderTable.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty());

        given(orderTableRepository.findById(orderTableId))
            .willReturn(Optional.of(orderTable));

        // when && then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(
            orderTableId, orderTableRequest));

        verify(orderTableRepository).findById(orderTableId);
        verify(orderTableRepository, times(0)).save(orderTable);
    }

    @DisplayName("주문 상태가 계산 완료가 아니면 주문 테이블의 주문 등록 가능 여부를 변경할 수 없다.")
    @Test
    void changeEmptyOrderStatusIsNotCompletion() {
        // given
        OrderTable orderTable = 주문_테이블_생성(1L, 0, true);
        Long orderTableId = orderTable.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty());

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableService.changeEmpty(orderTableId, orderTableRequest));

        verify(orderTableRepository).findById(orderTableId);
        verify(orderTableRepository, times(0)).save(orderTable);
    }

    @DisplayName("방문한 손님 수를 갱신할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = 주문_테이블_생성(1L, 5, false);
        OrderTable findOrderTable = 주문_테이블_생성(1L, 0, false);

        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty());

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(findOrderTable));
        given(orderTableRepository.save(findOrderTable)).willReturn(findOrderTable);
        // when
        OrderTableResponse savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        // then
        assertEquals(5, savedOrderTable.getNumberOfGuests());
    }

    @DisplayName("빈 테이블일 경우 방문한 손님 수를 갱신할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        OrderTable orderTable = 주문_테이블_생성(1L, 0, true);
        Long orderTableId = orderTable.getId();

        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getNumberOfGuests().getValue(),
            orderTable.isEmpty());

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableService.changeNumberOfGuests(orderTableId, orderTableRequest));
        verify(orderTableRepository).findById(orderTableId);
    }

    public static OrderTable 주문_테이블_생성(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
