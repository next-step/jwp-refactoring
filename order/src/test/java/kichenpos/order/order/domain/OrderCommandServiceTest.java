package kichenpos.order.order.domain;

import static kichenpos.order.order.sample.OrderSample.조리중인_후라이트치킨세트_두개_주문;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 커맨드 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderCommandServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator ordervalidator;

    @InjectMocks
    private OrderCommandService commandService;

    @Test
    @DisplayName("주문 저장")
    void save() {
        //given
        Order 조리중인_후라이트치킨세트_두개_주문 = 조리중인_후라이트치킨세트_두개_주문();

        //when
        commandService.save(조리중인_후라이트치킨세트_두개_주문);

        //then
        verify(orderRepository, only()).save(조리중인_후라이트치킨세트_두개_주문);
    }

    @Test
    @DisplayName("검증이 반드시 성공해야 함")
    void save_failedValidation_thrownException() {
        //given
        Order 조리중인_후라이트치킨세트_두개_주문 = 조리중인_후라이트치킨세트_두개_주문();
        doThrow(RuntimeException.class).when(ordervalidator)
            .validate(조리중인_후라이트치킨세트_두개_주문);

        //when
        ThrowingCallable saveCallable = () -> commandService.save(조리중인_후라이트치킨세트_두개_주문);

        //then
        assertAll(
            () -> assertThatExceptionOfType(RuntimeException.class).isThrownBy(saveCallable),
            () -> verify(orderRepository, never()).save(조리중인_후라이트치킨세트_두개_주문)
        );
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeStatus() {
        //given
        OrderStatus updatedStatus = OrderStatus.COMPLETION;

        Order mockOrder = mock(Order.class);
        when(orderRepository.order(anyLong())).thenReturn(mockOrder);

        //when
        commandService.changeStatus(1L, updatedStatus);

        //then
        verify(mockOrder, only()).changeStatus(updatedStatus);
    }
}
