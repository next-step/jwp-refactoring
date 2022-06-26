package kitchenpos.order.application;

import static kitchenpos.helper.OrderFixtures.주문_상태_계산완료_요청;
import static kitchenpos.helper.OrderFixtures.주문_상태_식사_요청;
import static kitchenpos.helper.OrderFixtures.주문_상태_조리_요청;
import static kitchenpos.helper.OrderFixtures.주문_요청_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.없는_주문_항목_요청;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_요청1;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_요청2;
import static kitchenpos.helper.TableFixtures.빈_테이블;
import static kitchenpos.helper.TableFixtures.주문_테이블;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("주문 관련 Service 기능 테스트")
@DataJpaTest
@Import({OrderService.class})
class OrderServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(주문_항목_요청1, 주문_항목_요청2));

        //when
        OrderResponse result = orderService.create(request, LocalDateTime.now());

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
        OrderTable orderTable = orderTableRepository.save(주문_테이블);
        OrderRequest emptyRequest = 주문_요청_만들기(orderTable.getId(), Collections.emptyList());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest, LocalDateTime.now()));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        OrderTable orderTable = orderTableRepository.save(주문_테이블);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(주문_항목_요청1, 없는_주문_항목_요청));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request, LocalDateTime.now()));
    }

    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        OrderTable emptyTable = orderTableRepository.save(빈_테이블);
        OrderRequest request = 주문_요청_만들기(emptyTable.getId(), Arrays.asList(주문_항목_요청1, 주문_항목_요청2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request, LocalDateTime.now()));
    }


    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderTable orderTable = orderTableRepository.save(주문_테이블);
        OrderResponse request = orderService
                .create(주문_요청_만들기(orderTable.getId(), Arrays.asList(주문_항목_요청1, 주문_항목_요청2)), LocalDateTime.now());

        //when
        OrderResponse result = orderService.changeOrderStatus(request.getId(), 주문_상태_식사_요청);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        OrderTable orderTable = orderTableRepository.save(주문_테이블);
        OrderResponse orderResponse = orderService
                .create(주문_요청_만들기(orderTable.getId(), Arrays.asList(주문_항목_요청1, 주문_항목_요청2)), LocalDateTime.now());
        orderService.changeOrderStatus(orderResponse.getId(), 주문_상태_계산완료_요청);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), 주문_상태_조리_요청));
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(주문_항목_요청1, 주문_항목_요청2));
        OrderResponse orderResponse = orderService.create(request, LocalDateTime.now());

        //when
        List<OrderResponse> results = orderService.findAllOrders();

        //then
        assertThat(results.stream().map(OrderResponse::getId).collect(Collectors.toList()))
                .contains(orderResponse.getId());
    }

}
