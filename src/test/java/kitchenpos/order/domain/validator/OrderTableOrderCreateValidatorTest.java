package kitchenpos.order.domain.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.validator.OrderTableOrderCreateValidator;
import kitchenpos.ordertable.infra.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kitchenpos.order.application.OrderServiceFixture.*;
import static kitchenpos.ordertable.application.OrderTableServiceTest.getOrderTable;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("주문 생성시 빈 테이블 유효성 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableOrderCreateValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableOrderCreateValidator validator;

    private OrderTable 빈_주문_테이블;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = getOrderTable(1L, false, 7);
        빈_주문_테이블 = getOrderTable(1L, true, 7);
    }

    @DisplayName("주문테이블이 없는 경우 유효하지 못하다.")
    @Test
    void validateFailNotFoundOrderTable() {
        // given
        doThrow(new IllegalArgumentException()).when(orderTableRepository).findById(anyLong());
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3),
                getOrderLineItem(2L, 4))
        );
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(order);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

    @DisplayName("주문테이블이 빈 상태인 경우 유효하지 못하다.")
    @Test
    void validateFailEmptyOrderTable() {
        // given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(빈_주문_테이블));
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3), getOrderLineItem(2L, 4))
        );
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(order);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

    @DisplayName("주문 생성시 주문테이블 빈상태 유효성 검사를 한다.")
    @Test
    void validate() {
        // given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3), getOrderLineItem(2L, 4))
        );
        final Executable executable = () -> validator.validate(order);
        // then
        assertDoesNotThrow(executable);
    }

}
