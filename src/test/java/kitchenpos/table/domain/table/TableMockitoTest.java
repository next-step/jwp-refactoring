package kitchenpos.table.domain.table;

import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableDomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Mockito - 테이블 관리")
class TableMockitoTest {
    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private TableService tableService;


    private void setUpMock() {
        orderRepository = mock(OrderRepository.class);
        orderTableRepository = mock(OrderTableRepository.class);
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    void createOrderTable() {
        // given
        setUpMock();
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(한식_테이블);

        // when
        final OrderTableResponse actual = tableService.saveOrderTable(한식_테이블_요청);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(한식_테이블_요청.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블 조회")
    void findAllOrderTable() {
        // given
        setUpMock();
        when(orderTableRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(한식_테이블, 양식_테이블)));

        // when
        final List<OrderTableResponse> actual = tableService.findAllOrderTable();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("방문 손님 숫자 변경")
    void changeGuestNumberSuccess() {
        // given
        setUpMock();
        OrderTableRequest 신규_요청 = OrderTableRequest.of(5, false);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(한식_테이블));
        한식_테이블.changeNumberOfGuests(5);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(한식_테이블);

        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(anyLong(), 신규_요청);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("주문 테이블 비우기")
    void changeEmptySuccess() {
        // given
        setUpMock();
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(한식_테이블));
        when(orderRepository.findAllByOrderTable(any(OrderTable.class)))
                .thenReturn(Lists.newArrayList());
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(한식_테이블);

        // when
        final OrderTableResponse actual = tableService.changeEmpty(anyLong());

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

}
