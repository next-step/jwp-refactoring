package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 생성 테스트")
    @Test
    void createTest() {
        // given
        OrderTableRequest request = new OrderTableRequest(3, true);
        OrderTable orderTable = new OrderTable(1l, 3);
        Mockito.when(orderTableRepository.save(any())).thenReturn(orderTable);

        // when
        OrderTableResponse actual = tableService.create(request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("전체 주문 테이블 리스트 조회 테스트")
    @Test
    void listTest() {
        // given
        OrderTable orderTable1 = new OrderTable(1l, 3);
        OrderTable orderTable2 = new OrderTable(1l, 3);
        Mockito.when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(2);
    }

    @DisplayName("빈 테이블로 상태 변경 테스트")
    @Test
    void changeEmptyTest() {
        // given
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);
        OrderTable orderTable = new OrderTable(null, 3);
        Mockito.when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        Mockito.when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        Mockito.when(orderTableRepository.save(any())).thenReturn(orderTable);

        // when
        OrderTableResponse actual = tableService.changeEmpty(1l, request);

        // then
        assertThat(actual).isNotNull();
    }


    @DisplayName("주문 테이블의 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(3);
        OrderTable orderTable = new OrderTable(1l, 3);
        Mockito.when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        Mockito.when(orderTableRepository.save(any())).thenReturn(orderTable);

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(1l, request);

        // then
        assertThat(actual).isNotNull();
    }

}
