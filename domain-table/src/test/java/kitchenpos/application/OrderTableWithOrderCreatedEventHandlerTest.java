package kitchenpos.application;

import kitchenpos.domain.OrderCreatedEvent;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.fixture.OrderTableFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderTableWithOrderCreatedEventHandlerTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderTableWithOrderCreatedEventHandler eventHandler;

    OrderTable 주문_테이블;
    OrderTable 빈_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTableFixture.of(4, false);
        빈_테이블 = OrderTableFixture.of(4, true);
    }

    @Test
    void 주문_발생_시_주문_테이블이_존재해야_한다() {
        // given
        OrderCreatedEvent 주문_이벤트 = new OrderCreatedEvent(1L);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> eventHandler.handle(주문_이벤트);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_빈_테이블이면_주문할_수_없다() {
        // given
        OrderCreatedEvent 주문_이벤트 = new OrderCreatedEvent(1L);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> eventHandler.handle(주문_이벤트);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}
