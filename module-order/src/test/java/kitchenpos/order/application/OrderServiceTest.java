package kitchenpos.order.application;

import static kitchenpos.order.helper.OrderFixtures.주문_상태_계산완료_요청;
import static kitchenpos.order.helper.OrderFixtures.주문_상태_식사_요청;
import static kitchenpos.order.helper.OrderFixtures.주문_상태_조리_요청;
import static kitchenpos.order.helper.OrderFixtures.주문_요청_만들기;
import static kitchenpos.order.helper.OrderLineItemFixtures.없는_주문_항목_요청;
import static kitchenpos.order.helper.OrderLineItemFixtures.주문_항목_요청1;
import static kitchenpos.order.helper.OrderLineItemFixtures.주문_항목_요청2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.helper.MenuBuilder;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.ValidateOrderTableEventHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("주문 관련 Service 기능 테스트")
@DataJpaTest
@Import({OrderService.class, ValidateOrderTableEventHandler.class})
class OrderServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        Long orderTableId = 9L;
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2));

        //when
        OrderResponse result = orderService.create(request, LocalDateTime.now());

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderTableId()).isNotNull();
        assertThat(result.getOrderLineItems().get(0).getMenuName()).isEqualTo("후라이드치킨");
        assertThat(result.getOrderLineItems().get(1).getPrice()).isEqualTo(16000);
    }

    @DisplayName("메뉴 이름, 가격이 변동되더라도 주문 내역은 동일해야한다.")
    @Test
    void create_update_menu() {

        //given
        Long orderTableId = 9L;
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2));
        OrderResponse createdOrder = orderService.create(request, LocalDateTime.now());

        //when
        menuRepository.save(MenuBuilder.builder().id(1L).name("변경 메뉴이름").price(17000).build());
        List<OrderResponse> results = orderService.findAllOrders();

        //then
        OrderResponse response = results.stream()
                .filter(orderResponse -> orderResponse.getId().equals(createdOrder.getId()))
                .findFirst().orElseThrow(IllegalArgumentException::new);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderTableId()).isNotNull();
        assertThat(response.getOrderLineItems().get(0).getMenuId()).isEqualTo(1L);
        assertThat(response.getOrderLineItems().get(0).getMenuName()).isEqualTo("후라이드치킨");
        assertThat(response.getOrderLineItems().get(0).getPrice()).isEqualTo(16000);
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        Long orderTableId = 9L;
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 없는_주문_항목_요청));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(request, LocalDateTime.now()))
                .withMessageContaining("등록 되어있지 않은 메뉴가 있습니다.");
    }

    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        Long emptyTableId = 1L;
        OrderRequest request = 주문_요청_만들기(emptyTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(request, LocalDateTime.now()))
                .withMessageContaining("빈테이블인 경우 주문을 등록 할 수 없습니다.");
    }


    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        Long orderTableId = 9L;
        OrderResponse request = orderService
                .create(주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2)), LocalDateTime.now());

        //when
        OrderResponse result = orderService.changeOrderStatus(request.getId(), 주문_상태_식사_요청);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        Long orderTableId = 9L;
        OrderResponse orderResponse = orderService
                .create(주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2)), LocalDateTime.now());
        orderService.changeOrderStatus(orderResponse.getId(), 주문_상태_계산완료_요청);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), 주문_상태_조리_요청))
                .withMessageContaining("계산완료상태에서 주문 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        Long orderTableId = 9L;
        OrderRequest request = 주문_요청_만들기(orderTableId, Arrays.asList(주문_항목_요청1, 주문_항목_요청2));
        OrderResponse orderResponse = orderService.create(request, LocalDateTime.now());

        //when
        List<OrderResponse> results = orderService.findAllOrders();

        //then
        assertThat(results.stream().map(OrderResponse::getId).collect(Collectors.toList()))
                .contains(orderResponse.getId());
    }

}
