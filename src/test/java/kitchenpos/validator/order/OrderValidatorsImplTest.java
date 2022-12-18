package kitchenpos.validator.order;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.validator.order.impl.OrderLineItemsSizeAndMenuCountValidator;
import kitchenpos.validator.order.impl.OrderTableExistAndEmptyValidator;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
class OrderValidatorsImplTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;
    private Order order = new Order(1L, Arrays.asList(new OrderLineItem(1L, 1, "메뉴명", new BigDecimal(16000)),
            new OrderLineItem(2L, 1, "메뉴명", new BigDecimal(16000))));
    private List<OrderValidator> orderValidators;

    @BeforeEach
    void setUp() {
        orderValidators = Arrays
                .asList(new OrderTableExistAndEmptyValidator(orderTableRepository),
                        new OrderLineItemsSizeAndMenuCountValidator(menuRepository));
    }

    @Test
    void 주문_등록시_주문_테이블이_등록되어_있지_않으면_등록_불가능하다() {
        given(orderTableRepository.findById(order.getOrderTableId())).willThrow(IllegalArgumentException.class);
        OrderValidatorsImpl orderValidatorImpl = new OrderValidatorsImpl(
                orderValidators);

        ThrowingCallable 등록되지_않은_주문_테이블_지정_할_경우 = () -> orderValidatorImpl.validateCreation(order);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_지정_할_경우);
    }

    @Test
    void 주문_등록시_주문_테이블이_비어있는_경우_지정_불가능하다() {
        OrderTable orderTable = new OrderTable(1, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        OrderValidatorsImpl orderValidatorImpl = new OrderValidatorsImpl(
                orderValidators);

        ThrowingCallable 주문_테이블이_비어있는_경우 = () -> orderValidatorImpl.validateCreation(order);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블이_비어있는_경우);
    }

    @Test
    void 주문_테이블에_주문을_등록한다2() {
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        OrderValidatorsImpl orderValidatorImpl = new OrderValidatorsImpl(
                orderValidators);

        ThrowingCallable 정상적으로_등록_가능한_경우 = () -> orderValidatorImpl.validateCreation(order);

        assertThatIllegalArgumentException().isThrownBy(정상적으로_등록_가능한_경우);
    }

    @Test
    void 주문_테이블에_주문을_등록한다() {
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.countByIdIn(any())).willReturn(2L);
        OrderValidatorsImpl orderValidatorImpl = new OrderValidatorsImpl(
                orderValidators);

        ThrowingCallable 정상적으로_등록_가능한_경우 = () -> orderValidatorImpl.validateCreation(order);

        assertThatNoException().isThrownBy(정상적으로_등록_가능한_경우);
    }

    @Test
    void 등록된_메뉴만_주문_등록을_할_수_있다() {
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.countByIdIn(any())).willReturn(2L);
        OrderValidatorsImpl orderValidatorImpl = new OrderValidatorsImpl(
                orderValidators);

        ThrowingCallable 등록된_메뉴의_갯수가_맞지_않는_경우 = () -> orderValidatorImpl.validateCreation(order);

        assertThatNoException().isThrownBy(등록된_메뉴의_갯수가_맞지_않는_경우);
    }
}
