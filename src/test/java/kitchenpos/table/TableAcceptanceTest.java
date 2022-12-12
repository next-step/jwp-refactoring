package kitchenpos.table;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
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
import java.util.stream.Stream;

import static kitchenpos.table.TableAcceptanceUtil.주문이_들어간_테이블_가져오기;
import static kitchenpos.tablegroup.TableGroupAcceptanceTest.단체_지정_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 빈_테이블;
    private OrderTableResponse 손님이_있는_테이블;
    private OrderTableResponse 손님이_있는_테이블2;

    @DisplayName("테이블 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> table() {
        return Stream.of(
                dynamicTest("빈 테이블을 등록한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_생성_요청(true, 0);

                    테이블_생성됨(response);
                    빈_테이블 = response.getBody();
                }),
                dynamicTest("손님이 있는 테이블을 등록한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_생성_요청(false, 5);

                    테이블_생성됨(response);
                    손님이_있는_테이블 = response.getBody();
                }),
                dynamicTest("손님이 있는 테이블을 등록한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_생성_요청(false, 5);

                    테이블_생성됨(response);
                    손님이_있는_테이블2 = response.getBody();
                }),
                dynamicTest("테이블 목록을 조회한다.", () -> {
                    ResponseEntity<List<OrderTableResponse>> response = 테이블_목록_조회_요청();

                    테이블_목록_응답됨(response);
                    테이블_목록_응답됨(response, 빈_테이블, 손님이_있는_테이블, 손님이_있는_테이블2);
                }),
                dynamicTest("주문 테이블의 손님 수를 변경한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_손님_수_변경_요청(손님이_있는_테이블, 5);

                    테이블_손님_수_변경됨(response);
                }),
                dynamicTest("주문 테이블의 손님 수를 0미만으로 변경한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_손님_수_변경_요청(손님이_있는_테이블, -1);

                    테이블_손님_수_변경_실패됨(response);
                }),
                dynamicTest("존재하지 않는 테이블의 손님 수를 변경한다.", () -> {
                    Long 존재하지_않는_테이블_id = Long.MAX_VALUE;

                    ResponseEntity<OrderTableResponse> response = 테이블_손님_수_변경_요청(존재하지_않는_테이블_id, 5);

                    테이블_손님_수_변경_실패됨(response);
                }),
                dynamicTest("빈 테이블의 손님 수를 변경한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_손님_수_변경_요청(빈_테이블, 5);

                    테이블_손님_수_변경_실패됨(response);
                }),
                dynamicTest("빈 테이블을 주문 테이블로 변경한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_비움_여부_변경_요청(빈_테이블, false);

                    테이블_비움_여부_변경됨(response);
                }),
                dynamicTest("다시 빈 테이블로 변경한다.", () -> {
                    ResponseEntity<OrderTableResponse> response = 테이블_비움_여부_변경_요청(빈_테이블, true);

                    테이블_비움_여부_변경됨(response);
                }),
                dynamicTest("단체 지정된 주문 테이블의 비움 여부를 변경한다.", () -> {
                    단체_지정_등록됨(손님이_있는_테이블, 손님이_있는_테이블2);

                    ResponseEntity<OrderTableResponse> response = 테이블_비움_여부_변경_요청(손님이_있는_테이블, true);

                    테이블_비움_여부_변경됨(response);
                }),
                dynamicTest("주문이 들어간 테이블의 비움 여부를 변경한다.", () -> {
                    OrderTableResponse 주문이_들어간_테이블 = 주문이_들어간_테이블_가져오기();

                    ResponseEntity<OrderTableResponse> response = 테이블_비움_여부_변경_요청(주문이_들어간_테이블, true);

                    테이블_비움_여부_변경_실패됨(response);
                })
        );
    }

    public static OrderTableResponse 테이블_등록됨(boolean empty, int numberOfGuests) {
        return 테이블_생성_요청(empty, numberOfGuests).getBody();
    }

    public static ResponseEntity<OrderTableResponse> 테이블_생성_요청(boolean empty, int numberOfGuests) {
        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);
        request.put("numberOfGuests", numberOfGuests);
        return restTemplate().postForEntity("/api/tables", request, OrderTableResponse.class);
    }

    public static ResponseEntity<List<OrderTableResponse>> 테이블_목록_조회_요청() {
        return restTemplate().exchange("/api/tables", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<OrderTableResponse>>() {});
    }

    public static ResponseEntity<OrderTableResponse> 테이블_손님_수_변경_요청(OrderTableResponse orderTable,
                                                                              int numberOfGuests) {
        return 테이블_손님_수_변경_요청(orderTable.getId(), numberOfGuests);
    }

    public static ResponseEntity<OrderTableResponse> 테이블_손님_수_변경_요청(Long orderTableId,
                                                                              int numberOfGuests) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderTableId", orderTableId);

        Map<String, Object> request = new HashMap<>();
        request.put("numberOfGuests", numberOfGuests);
        return restTemplate().exchange("/api/tables/{orderTableId}/number-of-guests", HttpMethod.PUT,
                new HttpEntity<>(request), OrderTableResponse.class, urlVariables);
    }

    public static ResponseEntity<OrderTableResponse> 테이블_비움_여부_변경_요청(OrderTableResponse orderTable,
                                                                                boolean empty) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderTableId", orderTable.getId());

        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);
        return restTemplate().exchange("/api/tables/{orderTableId}/empty", HttpMethod.PUT,
                new HttpEntity<>(request), OrderTableResponse.class, urlVariables);
    }

    public static void 테이블_생성됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    public static void 테이블_목록_응답됨(ResponseEntity<List<OrderTableResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 테이블_목록_응답됨(ResponseEntity<List<OrderTableResponse>> response,
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

    public static void 테이블_손님_수_변경됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 테이블_손님_수_변경_실패됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 테이블_비움_여부_변경됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 테이블_비움_여부_변경_실패됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
