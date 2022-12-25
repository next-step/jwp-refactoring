package kitchenpos.order;

import static kitchenpos.menu.MenuFixture.메뉴_등록;
import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_등록;
import static kitchenpos.order.OrderFixture.주문;
import static kitchenpos.order.OrderFixture.주문_목록_조회;
import static kitchenpos.order.OrderFixture.주문_상태_수정;
import static kitchenpos.product.ProductFixture.상품_등록;
import static kitchenpos.table.TableFixture.주문_테이블_추가;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    OrderTableResponse 일번테이블;
    OrderLineItemRequest 주문항목;

    /*
    Feature: 주문 관련 기능
        Background
            Given 메뉴 그룹이 등록되어 있음
            And 메뉴가 등록되어 있음
            And 주문 테이블 등록되어 있음
            And 주문 항목이 지정되어 있음
        Scenario: 주문 후 상태 관리
            When 주문 요청
            Then 주문 됨
            When 주문 상태 식사로 변경
            Then 주문 상태 식사로 변경 됨
            When 주문 목록 조회
            Then 주문 목록 조회 됨
        Scenario: 주문 후 상태 변경 실패
            When 주문 요청
            Then 주문 됨
            When 주문 상태 계산 완료로 변경
            Then 주문 상태 계산 완료로 됨
            When 주문 상태 식사로 변경
            Then 주문 상태 식사로 변경 실패
    */
    @BeforeEach
    public void setup() throws SQLException {
        super.setUp();
        일번테이블 = 주문_테이블_추가(new OrderTableRequest()).as(OrderTableResponse.class);

        MenuGroupResponse 추천_메뉴 = 메뉴_그룹_등록("추천 메뉴").as(MenuGroupResponse.class);
        ProductResponse 강정치킨 = 상품_등록("강정치킨", new BigDecimal(17_000)).as(ProductResponse.class);

        MenuResponse 더블강정치킨 = 메뉴_등록("더블강정치킨", new BigDecimal(19_000), 추천_메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(강정치킨.getId(), 2L)))
            .as(MenuResponse.class);

        주문항목 = new OrderLineItemRequest(더블강정치킨.getId(), 1L);
    }

    @Test
    void 주문후_상태관리() {
        //when
        ExtractableResponse<Response> orderResponse = 주문(일번테이블.getId(),
            Collections.singletonList(주문항목));
        //then
        assertThat(orderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        OrderResponse 식사_상태_주문 = 주문_상태_수정(orderResponse.as(OrderResponse.class).getId(),
            OrderStatus.MEAL).as(OrderResponse.class);
        //then
        assertThat(식사_상태_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

        //when
        List<OrderResponse> 주문_목록 = 주문_목록_조회().jsonPath().getList(".", OrderResponse.class);
        //then
        assertThat(주문_목록)
            .hasSize(1)
            .extracting(
                OrderResponse::getId,
                OrderResponse::getOrderStatus,
                OrderResponse::getOrderTableId,
                order -> order.getOrderLineItems().get(0).getMenuId(),
                order -> order.getOrderLineItems().get(0).getQuantity()
            )
            .containsExactly(tuple(
                식사_상태_주문.getId(),
                식사_상태_주문.getOrderStatus(),
                식사_상태_주문.getOrderTableId(),
                주문항목.getMenuId(),
                주문항목.getQuantity()
            ));
    }

    @Test
    void 주문후_상태변경_실패() {
        //when
        ExtractableResponse<Response> orderResponse = 주문(일번테이블.getId(),
            Collections.singletonList(주문항목));
        //then
        assertThat(orderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        OrderResponse 계산_완료_상태_주문 = 주문_상태_수정(orderResponse.as(OrderResponse.class).getId(),
            OrderStatus.COMPLETION).as(OrderResponse.class);
        //then
        assertThat(계산_완료_상태_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

        //when
        ExtractableResponse<Response> changeStatusResponse = 주문_상태_수정(
            orderResponse.as(OrderResponse.class).getId(),
            OrderStatus.MEAL);
        //then
        assertThat(changeStatusResponse.statusCode())
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}
