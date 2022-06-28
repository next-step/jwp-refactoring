package kitchenpos;

import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.AcceptanceTest.restTemplate;
import static kitchenpos.MenuAcceptanceUtil.신메뉴_강정치킨_가져오기;
import static kitchenpos.OrderAcceptanceTestUtil.주문_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

public final class TableAcceptanceUtil {

    private TableAcceptanceUtil() {
    }

    public static OrderTableResponse 주문이_들어간_테이블_가져오기() {
        OrderTableResponse 주문이_들어간_테이블 = 테이블_등록됨(false, 5);
        주문_등록됨(주문이_들어간_테이블, 신메뉴_강정치킨_가져오기());
        return 주문이_들어간_테이블;
    }

    public static OrderTableResponse 테이블_등록됨(boolean empty, int numberOfGuests) {
        return 테이블_생성_요청(empty, numberOfGuests).getBody();
    }

    static ResponseEntity<OrderTableResponse> 테이블_생성_요청(boolean empty, int numberOfGuests) {
        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);
        request.put("numberOfGuests", numberOfGuests);
        return restTemplate().postForEntity("/api/tables", request, OrderTableResponse.class);
    }

    static ResponseEntity<List<OrderTableResponse>> 테이블_목록_조회_요청() {
        return restTemplate().exchange("/api/tables", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<OrderTableResponse>>() {});
    }

    static ResponseEntity<OrderTableResponse> 테이블_손님_수_변경_요청(OrderTableResponse orderTable,
                                                                    int numberOfGuests) {
        return 테이블_손님_수_변경_요청(orderTable.getId(), numberOfGuests);
    }

    static ResponseEntity<OrderTableResponse> 테이블_손님_수_변경_요청(Long orderTableId,
                                                                    int numberOfGuests) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderTableId", orderTableId);

        Map<String, Object> request = new HashMap<>();
        request.put("numberOfGuests", numberOfGuests);
        return restTemplate().exchange("/api/tables/{orderTableId}/number-of-guests", HttpMethod.PUT,
                                       new HttpEntity<>(request), OrderTableResponse.class, urlVariables);
    }

    static ResponseEntity<OrderTableResponse> 테이블_비움_여부_변경_요청(OrderTableResponse orderTable,
                                                                     boolean empty) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderTableId", orderTable.getId());

        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);
        return restTemplate().exchange("/api/tables/{orderTableId}/empty", HttpMethod.PUT,
                                       new HttpEntity<>(request), OrderTableResponse.class, urlVariables);
    }

    static void 테이블_생성됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    static void 테이블_목록_응답됨(ResponseEntity<List<OrderTableResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 테이블_목록_응답됨(ResponseEntity<List<OrderTableResponse>> response,
                                  OrderTableResponse... orderTables) {
        List<Long> orderTableIds = response.getBody()
                                           .stream()
                                           .map(OrderTableResponse::getId)
                                           .collect(Collectors.toList());
        List<Long> expectedIds = Arrays.stream(orderTables)
                                       .map(OrderTableResponse::getId)
                                       .collect(Collectors.toList());
        assertThat(orderTableIds).containsExactlyElementsOf(expectedIds);
    }

    static void 테이블_손님_수_변경됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 테이블_손님_수_변경_실패됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 테이블_비움_여부_변경됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 테이블_비움_여부_변경_실패됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
