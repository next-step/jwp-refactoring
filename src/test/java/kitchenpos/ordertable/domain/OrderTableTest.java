package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.validator.OrderTableValidator;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Mock
    private OrderTable orderTable;

    private OrderTableValidator orderTableValidator;

    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroupId(1L);

        ThrowingCallable 이미_단체_지정이_된_테이블_수정 = () -> orderTable.changeEmpty(true, orderTableValidator);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_테이블_수정);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        List<Order> orders = Arrays.asList(order1, order2);
        given(orderRepository.findByOrderTableId(any())).willReturn(orders);
        given(order1.getOrderStatus()).willReturn(OrderStatus.COOKING.name());

        ThrowingCallable 조리_식사_상태의_주문이_포함_된_테이블_수정 = () -> orderTable.changeEmpty(true, orderTableValidator);

        assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_된_테이블_수정);
    }

    @Test
    void 비어있음_여부를_수정할_수_있다() {
        OrderTable orderTable = new OrderTable(1, false);
        List<Order> orders = Arrays.asList(order1, order2);
        given(orderRepository.findByOrderTableId(any())).willReturn(orders);
        given(order1.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
        given(order2.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
        given(orderRepository.findByOrderTableId(any())).willReturn(orders);

        orderTable.changeEmpty(true, orderTableValidator);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void 방문한_손님_수_수정시_빈_테이블일_경우_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        List<Order> orders = Arrays.asList(order1, order2);
        given(orderRepository.findByOrderTableId(any())).willReturn(orders);
        given(order1.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
        given(order2.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
        given(orderRepository.findByOrderTableId(any())).willReturn(orders);
        orderTable.changeEmpty(true, orderTableValidator);

        ThrowingCallable 빈_테이블인_상태에서_방문한_손님_수_수정 = () -> orderTable.changeNumberOfGuests(1);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블인_상태에서_방문한_손님_수_수정);
    }

    @Test
    void 방문한_손님_수_수정시_0명_이하로_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);

        ThrowingCallable 빈_테이블인_상태에서_방문한_손님_수_수정 = () -> orderTable.changeNumberOfGuests(-1);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블인_상태에서_방문한_손님_수_수정);
    }
}
