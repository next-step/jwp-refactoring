package kichenpos.order.order.domain;

import static kichenpos.order.order.sample.OrderSample.완료된_후라이트치킨세트_두개_주문;
import static kichenpos.order.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kichenpos.common.exception.InvalidStatusException;
import kichenpos.common.exception.NotFoundException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 상태 변경 서비스")
@ExtendWith(MockitoExtension.class)
class OrderStatusChangeServiceTest {

    @Mock
    OrderRepository orderRepository;
//    @Mock
//    OrderTableRepository tableRepository;

    @InjectMocks
    OrderStatusChangeService changeService;

    @Test
    @DisplayName("주문은 식사중 상태로 변경된다.")
    void change() {
        //given
        OrderStatus updatedStatus = OrderStatus.MEAL;

        Order order = spy(조리중인_후라이트치킨세트_두개_주문());
        when(orderRepository.order(anyLong())).thenReturn(order);

        //when
        changeService.change(1L, updatedStatus);

        //then
        assertAll(
            () -> verify(order, times(1)).changeStatus(updatedStatus)
//            () -> verify(tableRepository, never()).table(anyLong())
        );
    }

    @Test
    @DisplayName("주문이 완료 상태로 변경되면 테이블 상태도 완료로 변경된다.")
    void change_completedWithTable() {
        //given
        OrderStatus updatedStatus = OrderStatus.COMPLETION;

        Order order = spy(조리중인_후라이트치킨세트_두개_주문());
        when(orderRepository.order(anyLong())).thenReturn(order);

//        OrderTable orderTable = mock(OrderTable.class);
//        when(tableRepository.table(order.tableId())).thenReturn(orderTable);

        //when
        changeService.change(1L, updatedStatus);

        //then
        assertAll(
            () -> verify(order, times(1)).changeStatus(updatedStatus)
//            () -> verify(orderTable, only()).finish()
        );
    }


    @Test
    @DisplayName("변경 요청한 주문 id 는 저장되어 있어야 한다.")
    void change_notExistOrder_thrownException() {
        //given
        when(orderRepository.order(anyLong())).thenThrow(NotFoundException.class);

        //when
        ThrowingCallable changeCallable = () -> changeService.change(1L, OrderStatus.MEAL);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    void change_completedOrder_thrownException() {
        //given
        Order 완료된_후라이트치킨세트_두개_주문 = 완료된_후라이트치킨세트_두개_주문();
        when(orderRepository.order(anyLong())).thenReturn(완료된_후라이트치킨세트_두개_주문);

        //when
        ThrowingCallable changeCallable = () -> changeService.change(1L, OrderStatus.MEAL);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable);
    }
}
