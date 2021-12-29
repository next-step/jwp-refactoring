package kitchenpos.ordertable;

import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import java.util.List;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class TableAcceptanceFixtures {

    private static final String BASE_URL = "/api/tables";

    public static ResponseEntity<List<OrderTableResponse>> 테이블_전체_조회_요청() {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<OrderTableResponse>>() {
            });
    }

    public static ResponseEntity<OrderTableResponse> 테이블_등록_요청(
        OrderTableRequest orderTableRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, orderTableRequest, OrderTableResponse.class);
    }

    public static ResponseEntity<OrderTableResponse> 주문테이블_상태_변경_요청(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL + "/" + orderTableId + "/order_close",
            HttpMethod.PUT, new HttpEntity<>(orderTableRequest),
            OrderTableResponse.class);
    }

    public static ResponseEntity<OrderTableResponse> 주문테이블_방문자수_변경_요청(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL + "/" + orderTableId + "/number-of-guests",
            HttpMethod.PUT, new HttpEntity<>(orderTableRequest),
            OrderTableResponse.class);
    }

    public static OrderTableRequest 테이블_정의(int numOfGuests, boolean orderClose) {
        return new OrderTableRequest(numOfGuests, orderClose);
    }

    public static OrderTableRequest 테이블_정의(Long tableId) {
        return new OrderTableRequest(tableId);
    }

}
