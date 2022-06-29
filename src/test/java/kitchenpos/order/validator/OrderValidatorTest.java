package kitchenpos.order.validator;

import static kitchenpos.utils.DomainFixtureFactory.createOrderRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuService menuService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
    }

    @DisplayName("주문테이블이 비어있는 경우 테스트")
    @Test
    void validateNotEmpty() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        OrderTable orderTable = createOrderTable(1L, 2, true);
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(orderTable));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validate(orderRequest))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }

    @DisplayName("주문항목들 사이즈 불일치 테스트")
    @Test
    void validateOrderLineItemsSize() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(1L, 2L)));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuService.countByIdIn(Lists.newArrayList(1L))).willReturn(2);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validate(orderRequest))
                .withMessage("주문 항목들의 수와 조회된 메뉴 수가 일치하지 않습니다.");
    }
}
