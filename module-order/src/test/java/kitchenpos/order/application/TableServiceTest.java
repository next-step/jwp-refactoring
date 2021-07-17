package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.application.OrderTableValidator;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.exception.CannotChangeNumberOfGuestException;
import kitchenpos.order.exception.CannotChangeTableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    public static final int 두명 = 2;
    public static final int 세명 = 3;
    public static final boolean 비어있지않음 = false;
    public static final boolean 비어있음 = true;
    public static final boolean 진행중이_아님 = false;
    public static final boolean 진행중임 = true;
    public static final Long 주문테이블_ID = 1L;

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableValidator orderTableValidator;
    @InjectMocks
    private OrderTableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        // Given
        OrderTable 주문테이블 = new OrderTable(1L, 두명);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.save(any())).willReturn(주문테이블);

        // When
        tableService.create(주문테이블_요청);

        // Then
        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // Given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명, 비어있음));
        orderTables.add(new OrderTable(2L, 두명, 비어있음));
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // When & Then
        assertThat(tableService.list()).hasSize(2);
        verify(orderTableRepository, times(1)).findAll();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // Given
        OrderTable 주문테이블 = new OrderTable(주문테이블_ID, null, 두명);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        tableService.changeEmpty(주문테이블_ID, 주문테이블_요청);

        // Then
        verify(orderTableRepository, times(1)).findById(any());
    }

    @DisplayName("단체 지정된 주문테이블인 경우 빈 테이블로 변경이 불가능하다.")
    @Test
    void changeEmpty_Fail_01() {
        // Given
        OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 1L, 두명);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_ID, 주문테이블_요청))
            .isInstanceOf(CannotChangeTableEmptyException.class);

        // Then
        verify(orderTableRepository, times(1)).findById(any());
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // Given
        OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 1L, 두명, 비어있지않음);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        tableService.changeNumberOfGuests(주문테이블_ID, 주문테이블_요청);

        // Then
        verify(orderTableRepository, times(1)).findById(any());
    }

    @DisplayName("변경하려는 손님 수는 최소 1명 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_Fail_01() {
        // Given
        OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 1L, -1);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // When & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_ID, 주문테이블_요청))
            .isInstanceOf(CannotChangeNumberOfGuestException.class);
    }

    @DisplayName("빈 테이블의 주문 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_Fail_02() {
        // Given
        OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 1L, 두명, 비어있음);
        OrderTableRequest 주문테이블_요청 = OrderTableRequest.of(주문테이블);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_ID, 주문테이블_요청))
            .isInstanceOf(CannotChangeNumberOfGuestException.class);

        // Then
        verify(orderTableRepository, times(1)).findById(any());
    }

}
