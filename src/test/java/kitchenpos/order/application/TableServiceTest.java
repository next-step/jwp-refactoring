package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        OrderTableRequest orderTableRequest = mock(OrderTableRequest.class);
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTable.getId()).thenReturn(1L);
        when(orderTableRepository.save(any())).thenReturn(orderTable);

        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        assertThat(orderTableResponse).isNotNull();
        assertThat(orderTableResponse.getId()).isEqualTo(1L);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTable.getId()).thenReturn(1L);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));

        List<OrderTableResponse> orderTableResponses = tableService.list();

        assertThat(orderTableResponses).hasSize(1);
        assertThat(orderTableResponses.get(0).getId()).isEqualTo(1L);
    }

    @DisplayName("식사중과 요리중은 주문 테이블을 비울 수 없다.")
    @Test
    void empty_예외1() {
        OrderTable orderTable = new OrderTable(1L, null, 1, false);

        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, null, 1, false);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        식사중과_요리중_주문_테이블을_비울_경우_예외_발생함(orderTableRequest);
    }

    @DisplayName("그룹 으로 묶여있는 경우 테이블을 비을 수 없다.")
    @Test
    void empty_예외2() {
        OrderTable orderTable = new OrderTable(1L, mock(TableGroup.class), 1, false);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        그룹화된_주문테이블_비울경우_예외_발생함();
    }

    @DisplayName("주문 테이블을 비울 수 있다.")
    @Test
    void empty() {
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));
        tableService.changeEmpty(1L, mock(OrderTableRequest.class));
    }

    @DisplayName("등록된 테이블만 인원수를 변경할 수 있다.")
    @Test
    void changeGeust_예외1() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        등록된_테이블_아닐경우_예외_발생함();
    }

    @DisplayName("변경하려는 인원수는 0보다 커야한다.")
    @Test
    void changeGuest_예외2() {
        OrderTable orderTable = new OrderTable(4, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, null, -1, false);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        변경_인원수_0명_이하일_경우_예외_발생함(orderTableRequest);
    }

    @DisplayName("테이블이 비어있다면 인원수 변경할수 없다.")
    @Test
    void changeGuest_예외3() {
        OrderTable orderTable = new OrderTable(1, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, 1L, 3, false);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        테이블이_비어있을_경우_인원수_변경시_예외_발생함(orderTableRequest);
    }

    @DisplayName("인원수를 변경할 수 있다.")
    @Test
    void changeGuest() {
        OrderTable orderTable = new OrderTable(1L, mock(TableGroup.class), 1, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, 1L, 3, false);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        OrderTableResponse response = 인원수_변경_요청(1L, orderTableRequest);

        인원수_변경됨(response, orderTableRequest);
    }

    private void 식사중과_요리중_주문_테이블을_비울_경우_예외_발생함(OrderTableRequest orderTableRequest) {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
    }

    private void 그룹화된_주문테이블_비울경우_예외_발생함() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTableRequest.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹으로 묶여있는 경우 테이블을 비을 수 없습니다.");
    }

    private void 등록된_테이블_아닐경우_예외_발생함() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, mock(OrderTableRequest.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 변경_인원수_0명_이하일_경우_예외_발생함(OrderTableRequest orderTableRequest) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경하려는 인원수는 0보다 커야합니다.");
    }

    private void 테이블이_비어있을_경우_인원수_변경시_예외_발생함(OrderTableRequest orderTableRequest) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블은 인원수 변경이 불가합니다.");
    }

    private OrderTableResponse 인원수_변경_요청(Long orderTableId, OrderTableRequest request) {
        return tableService.changeNumberOfGuests(1L, request);
    }

    private void 인원수_변경됨(OrderTableResponse response, OrderTableRequest orderTableRequest) {
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }
}
