package kitchenpos.order.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableExistAndEmptyValidator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
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
    private OrderValidatorsImpl orderValidators;

    @BeforeEach
    void setUp() {
        orderValidators = new OrderValidatorsImpl(new OrderLineItemsSizeAndMenuCountValidator(menuRepository),
                new OrderTableExistAndEmptyValidator(orderTableRepository));
    }

    @Test
    void 주문_등록시_주문_테이블이_등록되어_있지_않으면_등록_불가능하다() {
        BDDMockito.given(orderTableRepository.findById(order.getOrderTableId()))
                .willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문_테이블_지정_할_경우 = () -> orderValidators.validateCreation(order);

        Assertions.assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_지정_할_경우);
    }

    @Test
    void 주문_등록시_주문_테이블이_비어있는_경우_지정_불가능하다() {
        OrderTable orderTable = new OrderTable(1, true);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(orderTable));

        ThrowingCallable 주문_테이블이_비어있는_경우 = () -> orderValidators.validateCreation(order);

        Assertions.assertThatIllegalArgumentException().isThrownBy(주문_테이블이_비어있는_경우);
    }

    @Test
    void 주문_테이블에_주문을_등록한다2() {
        OrderTable orderTable = new OrderTable(1, false);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any())).willReturn(1L);

        ThrowingCallable 정상적으로_등록_가능한_경우 = () -> orderValidators.validateCreation(order);

        Assertions.assertThatIllegalArgumentException().isThrownBy(정상적으로_등록_가능한_경우);
    }

    @Test
    void 주문_테이블에_주문을_등록한다() {
        OrderTable orderTable = new OrderTable(1, false);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any())).willReturn(2L);

        ThrowingCallable 정상적으로_등록_가능한_경우 = () -> orderValidators.validateCreation(order);

        Assertions.assertThatNoException().isThrownBy(정상적으로_등록_가능한_경우);
    }

    @Test
    void 등록된_메뉴만_주문_등록을_할_수_있다() {
        OrderTable orderTable = new OrderTable(1, false);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any())).willReturn(2L);

        ThrowingCallable 등록된_메뉴의_갯수가_맞지_않는_경우 = () -> orderValidators.validateCreation(order);

        Assertions.assertThatNoException().isThrownBy(등록된_메뉴의_갯수가_맞지_않는_경우);
    }
}
