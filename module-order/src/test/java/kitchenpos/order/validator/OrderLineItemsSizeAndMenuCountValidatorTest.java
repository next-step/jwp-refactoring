package kitchenpos.order.validator;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderLineItemsSizeAndMenuCountValidatorTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private Order order;

    @Test
    void 주문_등록시_등록_된_메뉴만_지정_가능하다() {
        OrderLineItemsSizeAndMenuCountValidator validator = new OrderLineItemsSizeAndMenuCountValidator(menuRepository);
        BDDMockito.given(menuRepository.countByIdIn(ArgumentMatchers.any())).willReturn(2L);
        BDDMockito.given(order.getOrderLineItems())
                .willReturn(Collections.singletonList(new OrderLineItem(1L, 1, "메뉴명", new BigDecimal(16000))));

        ThrowingCallable 등록_되지_않은_메뉴가_포함_될_경우 = () -> validator.validate(order);

        Assertions.assertThatIllegalArgumentException().isThrownBy(등록_되지_않은_메뉴가_포함_될_경우);
    }
}
