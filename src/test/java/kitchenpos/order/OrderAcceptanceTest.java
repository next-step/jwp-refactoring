package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.Product.ProductAccteptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.MenuAcceptanceTest.메뉴_등록되어_있음;
import static kitchenpos.table.tableAcceptanceTest.주문_테이블_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderRequest orderRequest;
    private OrderLineItemRequest orderLineItemRequest;
    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuResponse menuResponse = 메뉴_등록되어_있음(generateMenuRequest());
        OrderTableResponse orderTableResponse = 주문_테이블_등록_되어있음(new OrderTableRequest(1, false));
        orderLineItemRequest = new OrderLineItemRequest(menuResponse.getId(),11l);
        orderRequest = new OrderRequest(orderTableResponse.getId(), "COOKING", Arrays.asList(orderLineItemRequest));
    }

    private MenuRequest generateMenuRequest() {
        ProductResponse givenProductOne = ProductAccteptanceTest.상품_등록되어_있음(new ProductRequest("커피", BigDecimal.valueOf(3000)));
        ProductResponse givenProductTwo = ProductAccteptanceTest.상품_등록되어_있음(new ProductRequest("라면",BigDecimal.valueOf(5000)));
        MenuProductRequest firstMenuProduct = new MenuProductRequest(givenProductOne.getId(), 1);
        MenuProductRequest secondMenuProduct = new MenuProductRequest(givenProductTwo.getId(), 2);
        Arrays.asList(firstMenuProduct, secondMenuProduct);
        MenuGroupResponse givenMenuGroup = MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음(new MenuGroupRequest("테스트메뉴그룹"));
        return new MenuRequest("일번메뉴", BigDecimal.valueOf(1000), givenMenuGroup.getId(), Arrays.asList(firstMenuProduct, secondMenuProduct));
    }

    @DisplayName("dto와 jpa를 사용하여 주문을 등록할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 주문_등록_요청(orderRequest);

        //then
        정상_등록(response);
        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertThat(orderResponse.getOrderTableId()).isEqualTo(orderRequest.getOrderTableId());
    }


    @DisplayName("dto와 jpa를 사용하여 주문을 조회할 수 있다")
    @Test
    void listTest() {
        //when
        ExtractableResponse<Response> response = 주문_조회_요청();

        //then
        정상_처리(response);
    }

    private ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .body(orderRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders/temp")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders/temp")
                .then().log().all()
                .extract();
    }


}
