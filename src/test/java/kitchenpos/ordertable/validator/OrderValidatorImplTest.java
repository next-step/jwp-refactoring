package kitchenpos.ordertable.validator;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static kitchenpos.order.fixture.OrderLineItemTestFixture.주문정보목록;
import static kitchenpos.order.fixture.OrderLineItemTestFixture.주문정보요청;
import static kitchenpos.order.fixture.OrderTestFixture.주문;
import static kitchenpos.ordertable.fixture.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorImplTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderValidatorImpl orderValidator;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItemRequest 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItemRequest 짬뽕_탕수육_1인_메뉴_세트주문;
    private OrderRequest 주문1_요청;
    private OrderRequest 주문2_요청;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    public void setUp() {
        주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블1_요청());
        주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블2_요청());
        짜장면_탕수육_1인_메뉴_세트주문 = 주문정보요청(1L, 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = 주문정보요청(2L, 1);
        주문1_요청 = 주문(주문테이블1.getId(), null, null, Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문));
        주문2_요청 = 주문(주문테이블2.getId(), null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        주문1 = Order.of(1L, 주문정보목록(주문1_요청.getOrderLineItemsRequest()));
        주문2 = Order.of(2L, 주문정보목록(주문2_요청.getOrderLineItemsRequest()));
    }


    @DisplayName("주문테이블이 비어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void validate() {
        // given
        주문테이블1.changeEmpty(true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validate(주문1_요청))
                .withMessage(ErrorMessage.EMPTY_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문을 생성할 떄, 주문 항목이 비어있으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        OrderRequest order = 주문(주문테이블1.getId(), null, null, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validate(order));
    }

    @DisplayName("주문을 생성할 때, 주문 항목 내에 등록되지 않은 메뉴가 있다면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validate((주문1_요청)));
    }

    @DisplayName("주문을 생성할 때, 주문 테이블이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validate((주문1_요청)));
    }

    @DisplayName("주문상태가 조리중이거나 식사중이면 IllegalArgumentException을 반환한다.")
    @Test
    void validateOrderStatus() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validateOnGoingOrderStatus(Arrays.asList(주문1, 주문2)))
                .withMessage(ErrorMessage.TABLE_HAVE_ONGOING_ORDER.getMessage());
    }
}
