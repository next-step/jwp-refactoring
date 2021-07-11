package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Arrays;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderRequest orderRequest;
    private OrderLineItemRequest orderLineItemRequest;
    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItemRequest = new OrderLineItemRequest(1l,1l,1l,11l);
        orderRequest = new OrderRequest(1l, "COOKING", Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("dto와 jpa를 사용하여 주문을 등록할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 주문_등록_요청(orderRequest);

        //then
        정상_등록(response);
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


}
