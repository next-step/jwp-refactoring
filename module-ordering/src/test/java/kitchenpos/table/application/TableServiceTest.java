package kitchenpos.table.application;

import kitchenpos.ordering.domain.OrderRepository;
import kitchenpos.table.application.TableService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTableValidator orderTableValidator;

    private Long orderTable1Id = 1L;
    private Long orderTable1TableGroupId = 1L;
    private int orderTable1NumberOfGuests = 4;
    private boolean orderTable1Empty = false;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository, orderTableValidator);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable1 = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableResponse = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);

        when(orderTableRepository.save(any())).thenReturn(orderTableResponse);

        OrderTableResponse response = tableService.create(orderTableRequest);

        Assertions.assertThat(response.getId()).isEqualTo(orderTableResponse.getId());
        Assertions.assertThat(response.getTableGroupId()).isEqualTo(orderTableResponse.getTableGroupId());
        Assertions.assertThat(response.getNumberOfGuests()).isEqualTo(orderTableResponse.getNumberOfGuests());
        Assertions.assertThat(response.isEmpty()).isEqualTo(orderTableResponse.isEmpty());
    }

    @DisplayName("테이블 전체를 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 0, true);

        when(orderTableRepository.findAll()).thenReturn(
                Arrays.asList(
                        orderTable1,
                        orderTable2
                )
        );

        List<OrderTableResponse> responses = tableService.list();
        Assertions.assertThat(responses).hasSize(2);
        assertThat(responses.stream().map(OrderTableResponse::getId))
                .contains(orderTable1.getId(), orderTable2.getId());
    }

    @DisplayName("테이블을 빈테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(orderTable1NumberOfGuests, !orderTable1Empty);

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTableSaved));
//        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);

        OrderTableResponse response = tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);

        Assertions.assertThat(response.getId()).isEqualTo(orderTableSaved.getId());
        Assertions.assertThat(response.getTableGroupId()).isEqualTo(orderTableSaved.getTableGroupId());
        Assertions.assertThat(response.getNumberOfGuests()).isEqualTo(orderTableSaved.getNumberOfGuests());
        Assertions.assertThat(response.isEmpty()).isEqualTo(orderTableSaved.isEmpty());
    }

    @DisplayName("주문테이블이 등록되어 있어야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_테이블상태를_변경할_수_없다_1() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(orderTable1NumberOfGuests, true);

        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(changeNumberOfGuests, false);

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTableSaved));

        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);

        Assertions.assertThat(response.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
    }

    @DisplayName("손님 숫자는 0 이상이어야 한다.")
    @Test
    void 주문테이블의_손님_수가_올바르지_않으면_손님_수를_변경할_수_없다() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, 0, true);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(changeNumberOfGuests, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 등록되어 있어야 한다..")
    @Test
    void 주문테이블이_올바르지_않으면_인원을_변경할_수_없다_1() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(changeNumberOfGuests, false);

        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블은 비어있지 않아야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_인원을_변경할_수_없다_2() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, true);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(changeNumberOfGuests, false);

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTableSaved));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
