package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문")
public class OrderAcceptanceTest extends AcceptanceTest {

    OrderTable 주문테이블;
    private ProductResponse 생맥주;
    private ProductResponse 닭강정;
    private MenuGroupResponse 일식;

    private Menu 닭강정_안주;
    private Menu 닭강정_세트;


    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블 = 주문_테이블이_존재함(new OrderTable(null, 3, false));

        생맥주 = 상품이_등록되어_있음(new ProductRequest("생맥주", BigDecimal.valueOf(2000)));
        닭강정 = 상품이_등록되어_있음(new ProductRequest("닭강정", BigDecimal.valueOf(3000)));
        일식 = 메뉴그룹이_등록되어있음(new MenuGroupRequest("일식"));

        MenuProduct 닭강정_안주_닭강정 = new MenuProduct(1L, 2L, 닭강정.getId(), 2);
        List<MenuProduct> 닭강정_안주_상품들 = Collections.singletonList(닭강정_안주_닭강정);
        닭강정_안주 = 메뉴가_등록_되어_있음(new Menu("닭강정_안주", BigDecimal.valueOf(3000), 일식.getId(), 닭강정_안주_상품들));

        MenuProduct 닭강정_세트_닭강정 = new MenuProduct(1L, 2L, 닭강정.getId(), 2);
        MenuProduct 닭강정_세트_생맥주 = new MenuProduct(1L, 2L, 생맥주.getId(), 1);
        List<MenuProduct> 닭강정_세트_상품들 = Arrays.asList(닭강정_세트_닭강정, 닭강정_세트_생맥주);

        닭강정_세트 = 메뉴가_등록_되어_있음(new Menu("닭강정_세트", BigDecimal.valueOf(4000), 일식.getId(), 닭강정_세트_상품들));
    }


    /*
        -주문 관리
        background:
            given 주문 테이블 존재함
             and 상품이 등록되어있음
             and 메뉴가 등록되어있음
             and 메뉴가 등록되어있음
        Scenario: 주문관리
            given 주문을 생성한다.
            when  주문을 요청
            then  주문이 요청됨
            when  주문 목록 조회
            then  요청한 주문이 조회됨
            when  요청한 주문 상태 완료로 변경
            then  주문의 상태가 변경됨
            when  주문목록조회
            then  주문의 상태가 완료됨 상태를 조회됨
    */
    @Test
    @DisplayName("주문관리")
    void orderManage() {

        //given
        OrderLineItem 주문1 = new OrderLineItem(1L, 닭강정_세트.getId(),1);
        OrderLineItem 주문2 = new OrderLineItem(2L, 닭강정_안주.getId(),2);
        List<OrderLineItem> 주문항목 = Arrays.asList(주문1, 주문2);
        Order order = new Order(주문테이블.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), 주문항목);

        //when
        ExtractableResponse<Response> createResponse = 주문을_요청(order);

        //then
        주문_요청됨(createResponse);

        //when
        ExtractableResponse<Response> orderResponse = 주문_목록_조회();
        //then
        요청한_주문이_조회됨(createResponse, orderResponse);

        //when
        final ExtractableResponse<Response> updateResponse = 주문의_상태를_완료로_변경_요청(createResponse);

        //then
        주문의_상태가_변경됨(updateResponse);

        //when
        final ExtractableResponse<Response> reSearchResponse = 주문_목록_조회();
        //then
        주문완료_상태로_변경됨(createResponse.as(Order.class).getId() ,reSearchResponse);


    }


    private ExtractableResponse<Response> 주문을_요청(Order order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then()
                .log().all()
                .extract();
    }

    private void 주문_요청됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    private ExtractableResponse<Response> 주문_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then()
                .log().all()
                .extract();
    }

    private void 요청한_주문이_조회됨(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> orderResponse) {
        assertThat(orderResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderResponse.jsonPath().getList("id", Long.class)).contains(createResponse.as(Order.class).getId());
    }
    private void 주문완료_상태로_변경됨(long orderId, ExtractableResponse<Response> reSearchResponse) {
        assertThat(reSearchResponse.jsonPath().getString("find{it -> it.id==" + orderId + "}.orderStatus"))
                .isEqualTo(OrderStatus.COMPLETION.name());
    }

    private void 주문의_상태가_변경됨(ExtractableResponse<Response> updateResponse) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문의_상태를_완료로_변경_요청(ExtractableResponse<Response> createResponse) {
        Order updateStatusOrder = new Order();
        updateStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateStatusOrder)
                .pathParam("orderId", createResponse.as(Order.class).getId())
                .when().put("/api/orders/{orderId}/order-status")
                .then()
                .log().all()
                .extract();
    }

    private OrderTable 주문_테이블이_존재함(OrderTable orderTable) {
        return TableAcceptanceTest.주문가능한_테이블을_요청한다(orderTable).as(OrderTable.class);
    }

    private ProductResponse 상품이_등록되어_있음(ProductRequest product) {
        return ProductAcceptanceTest.상품_등록을_요청(product).as(ProductResponse.class);
    }

    private MenuGroupResponse 메뉴그룹이_등록되어있음(MenuGroupRequest menuGroupRequest) {
        return MenuGroupAcceptanceTest.메뉴그룹_등록을_요청(menuGroupRequest).as(MenuGroupResponse.class);
    }

    private Menu 메뉴가_등록_되어_있음(Menu menu) {
        return MenuAcceptanceTest.메뉴등록을_요청(menu).as(Menu.class);
    }

}
