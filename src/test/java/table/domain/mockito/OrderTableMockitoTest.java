package table.domain.mockito;

import static fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import order.repository.*;
import table.application.*;
import table.dto.*;
import table.repository.*;

@DisplayName("주문 테이블 관련(Mockito)")
class OrderTableMockitoTest {
    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderTableRepository = mock(OrderTableRepository.class);
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTest() {
        when(orderTableRepository.save(any())).thenReturn(주문테이블_4명);
        assertThat(tableService.saveOrderTable(주문테이블_4명_요청)).isInstanceOf(OrderTableResponse.class);
    }

    @DisplayName("주문 테이블 조회하기")
    @Test
    void findAllTest() {
        when(orderTableRepository.findAll()).thenReturn(Lists.newArrayList(주문테이블_4명, 주문테이블_6명));
        assertThat(tableService.findAll()).containsExactly(
            OrderTableResponse.of(주문테이블_4명), OrderTableResponse.of(주문테이블_6명)
        );
    }

    @DisplayName("방문한 손님수 변경하기")
    @Test
    void changeNumberOfGuestsTest() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문테이블_4명));

        주문테이블_4명.changeNumberOfGuests(6);
        when(orderTableRepository.save(any())).thenReturn(주문테이블_4명);

        OrderTableRequest orderTableRequest = OrderTableRequest.of(6, false);
        assertThat(tableService.changeNumberOfGuests(anyLong(), orderTableRequest).getNumberOfGuests()).isEqualTo(6);
    }

    @DisplayName("주문 테이블 청소하기")
    @Test
    void cleanOrderTable() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문테이블_4명));
        when(orderTableRepository.save(any())).thenReturn(주문테이블_4명);

        assertThat(tableService.cleanTable(anyLong()).isEmpty()).isEqualTo(true);
    }
}
