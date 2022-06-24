package kitchenpos.order.application;

import static kitchenpos.helper.OrderFixtures.주문_요청_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_요청_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("주문 관련 Service 기능 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(1L, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(2L, 2);
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        OrderResponse result = orderService.create(request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderTableId()).isNotNull();
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isNotNull();
        assertThat(result.getOrderLineItems().get(1).getOrderId()).isNotNull();
    }

    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없다.")
    @Test
    void create_empty() {
        //given
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderRequest emptyRequest = 주문_요청_만들기(orderTable.getId(), Collections.emptyList());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(1L, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(999999999L, 2);
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(1L, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(2L, 2);
        OrderTable emptyTable = orderTableRepository.save(테이블_만들기(0, true));
        OrderRequest request = 주문_요청_만들기(emptyTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }


    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(1L, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(2L, 2);
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderResponse request = orderService
                .create(주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2)));

        //when
        OrderResponse result = orderService.changeOrderStatus(request.getId(), 주문_요청_만들기(OrderStatus.MEAL));

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(1L, 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(2L, 2);
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderResponse orderResponse = orderService
                .create(주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2)));
        orderService.changeOrderStatus(orderResponse.getId(), 주문_요청_만들기(OrderStatus.COMPLETION));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), 주문_요청_만들기(OrderStatus.COOKING)));
    }

}
