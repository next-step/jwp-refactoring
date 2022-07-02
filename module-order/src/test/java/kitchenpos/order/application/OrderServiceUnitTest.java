package kitchenpos.order.application;

import static kitchenpos.order.helper.OrderRequestFixtures.주문_요청_만들기;
import static kitchenpos.order.helper.OrderLineItemRequestFixtures.주문_항목_요청_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.helper.MenuBuilder;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.OrderCreateEvent;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("주문 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, menuRepository, eventPublisher);
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        Long mendId = 1L;
        Long orderTableId = 1L;
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(mendId, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(mendId, 2);
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(orderLineItem1, orderLineItem2));
        Menu menu = MenuBuilder.builder().name("메뉴1").build();
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(menu));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request, LocalDateTime.now()))
                .withMessageContaining("등록 되어있지 않은 메뉴가 있습니다");
    }


    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        Menu menu1 = MenuBuilder.builder().id(1L).name("메뉴1").build();
        Menu menu2 = MenuBuilder.builder().id(2L).name("메뉴2").build();
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu1.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu2.getId(), 2);
        Long orderTableId = 1L;
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(menu1, menu2));
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(orderLineItem1, orderLineItem2));
        willThrow(new IllegalArgumentException("빈 테이블인 경우 주문을 등록 할 수 없다."))
                .given(eventPublisher).publishEvent(any(OrderCreateEvent.class));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(request, LocalDateTime.now()))
                .withMessageContaining("빈 테이블인 경우 주문을 등록 할 수 없다.");
    }


}
