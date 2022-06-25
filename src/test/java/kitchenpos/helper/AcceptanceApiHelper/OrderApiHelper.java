package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.http.MediaType;

public class OrderApiHelper {

    public static ExtractableResponse<Response> 주문_생성하기(long 테이블_ID, List<OrderLineItem> 주문메뉴) {
        Order 오더 = new Order();
        오더.setOrderTableId(테이블_ID);
        오더.setOrderLineItems(주문메뉴);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(오더)
            .when().post("/api/orders")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 오더_리스트_조회하기() {
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경하기(String 오더_상태, long 오더_번호) {
        Map<String, String> 요청전문 = new HashMap<>();
        요청전문.put("orderStatus", 오더_상태);

        return RestAssured
            .given().log().all()
            .body(요청전문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/orders/" + 오더_번호 + "/order-status")
            .then().log().all().
            extract();
    }
}
