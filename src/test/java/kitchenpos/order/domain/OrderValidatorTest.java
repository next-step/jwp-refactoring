package kitchenpos.order.domain;

import static kitchenpos.menu.domain.MenuProductTest.*;
import static kitchenpos.menu.domain.MenuTest.*;
import static kitchenpos.order.domain.OrderLineItemDetailTest.*;
import static kitchenpos.order.domain.OrderLineItemTest.*;
import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.quantity.domain.Quantity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.generic.exception.MenuMismatchException;
import kitchenpos.ordertable.domain.OrderTableRepository;

@DisplayName("주문 밸리데이터 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @InjectMocks
    OrderValidator validator;

    @Mock
    MenuRepository menuRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    Order 새로운_주문;

    @Test
    @DisplayName("실패: 빈 테이블에는 주문할 수 없음")
    void validate_fail1() {
        새로운_주문 = OrderTest.order(1L, OrderLineItems.of(테이블9주문_1));
        assertThatThrownBy(() -> validator.validate(새로운_주문, 테이블6))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("실패: 상세내역 없어 주문할 수 없음")
    void validate_fail2() {
        새로운_주문 = OrderTest.order(1L, OrderLineItems.of(Collections.emptyList()));

        assertThatThrownBy(() -> validator.validate(새로운_주문, 테이블9_사용중))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: 메뉴명 변경 되어 주문할 수 없음")
    void validate_fail3() {
        OrderLineItem 이름이_바뀐_메뉴 = of(1L, 반반치킨_메뉴.getId(), "새로운_이름", 반반치킨_메뉴.getPrice(),
            Quantity.valueOf(1), OrderLineItemDetails.of(반반치킨_주문내역_상세));
        when(menuRepository.findById(반반치킨_메뉴.getId())).thenReturn(Optional.of(반반치킨_메뉴));

        새로운_주문 = OrderTest.order(1L, OrderLineItems.of(이름이_바뀐_메뉴));
        assertThatThrownBy(() -> validator.validate(새로운_주문, 테이블9_사용중))
            .isInstanceOf(MenuMismatchException.class);
    }

    @Test
    @DisplayName("실패: 세부항목 변경되어 주문할 수 없음")
    void validate_fail4() {
        OrderLineItemDetail 가격이_바뀐_상세내역
            = new OrderLineItemDetail(반반치킨.getId(), 반반치킨.getName(), Price.valueOf(50000), MP3반반치킨.getQuantity());
        OrderLineItem 상세내역이_바뀐_메뉴 = of(1L, 반반치킨_메뉴.getId(), 반반치킨_메뉴.getName(), 반반치킨_메뉴.getPrice(),
            Quantity.valueOf(1), OrderLineItemDetails.of(가격이_바뀐_상세내역));

        새로운_주문 = OrderTest.order(1L, OrderLineItems.of(상세내역이_바뀐_메뉴));
        assertThatThrownBy(() -> validator.validate(새로운_주문, 테이블6))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("실패: 완결 된 주문의 상태를 변경할 수 없음")
    void checkChangeable_fail1() {
        새로운_주문 = mock(Order.class);
        when(새로운_주문.isCompleted()).thenReturn(true);

        assertThatThrownBy(() -> validator.validateChangeOrderStatus(새로운_주문))
            .isInstanceOf(IllegalOperationException.class);
    }
}
