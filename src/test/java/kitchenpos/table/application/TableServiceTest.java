package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(3), true);
    private final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);

    @Mock
    private OrderService orderService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블생성테스트")
    @Test
    void createTableTest() {
        //given
        given(orderTableRepository.save(any(OrderTable.class)))
                .willReturn(주문테이블1);

        //when
        OrderTableResponse result = tableService.create(OrderTableToOrderTableRequest(주문테이블1));

        //then
        assertAll(
                () -> assertThat(result.getId())
                        .isEqualTo(주문테이블1.getId()),
                () -> assertThat(result.getNumberOfGuests())
                        .isEqualTo(주문테이블1.getNumberOfGuests().getValue())
        );
    }

    private OrderTableRequest OrderTableToOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty());
    }

    @DisplayName("빈 상태로 변경테스트")
    @Test
    void changeEmptyTableTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(2), false);
        final Order 주문 = new Order(1L, orderTable.getId(), OrderStatus.COMPLETION.name(),
                LocalDateTime.now(), Arrays.asList());

        when(orderTableRepository.findById(orderTable.getId()))
                .thenReturn(Optional.ofNullable(orderTable));
        when(orderService.findOrderByOrderTableId(orderTable.getId()))
                .thenReturn(주문);

        //when
        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), true);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 빈 테이블 변경 오류 테스트")
    @Test
    void notExistIdChangeEmptyTableExceptionTest() {
        //given
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 빈 테이블로 변경 오류 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void cookingOrMealChangeEmptyTableExceptionTest(String orderStatus) {
        //given
        final Order 주문 = new Order(1L, 주문테이블1.getId(), orderStatus,
                LocalDateTime.now(), Arrays.asList());

        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderService.findOrderByOrderTableId(주문테이블1.getId()))
                .thenReturn(주문);

        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("게스트숫자 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        int changeGuestNumber = 2;
        final OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(3), false);

        when(orderTableRepository.findById(주문테이블.getId()))
                .thenReturn(Optional.ofNullable(주문테이블));

        //when
        final OrderTableResponse result = tableService.changeNumberOfGuests(주문테이블.getId(), changeGuestNumber);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(changeGuestNumber);
    }

    @DisplayName("게스트숫자 0보다 작은 값으로 변경 오류 테스트")
    @Test
    void changeNumberOfGuestsUnderZeroExceptionTest() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 id로 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestNotExistTableIdExceptionTest() {
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블에서 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestEmptyTableIdExceptionTest() {
        //given
        final OrderTable 빈테이블 = new OrderTable(1L, null, new NumberOfGuests(3), true);
        when(orderTableRepository.findById(빈테이블.getId()))
                .thenReturn(Optional.ofNullable(빈테이블));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
