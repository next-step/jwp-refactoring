package kitchenpos.ui.table;

import static kitchenpos.ui.menu.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.ui.menu.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.ui.order.OrderAcceptanceTest.주문_등록_되어있음;
import static kitchenpos.ui.product.ProductAcceptanceTest.상품_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("테이블 관련 기능 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest {
    @TestFactory
    @DisplayName("테이블 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("빈 테이블을 등록요청하면 테이블이 등록된다.", () -> {
                    ResponseEntity<OrderTableResponse>빈_테이블_등록_요청_결과 = 테이블_등록_요청(0, true);

                    테이블_등록됨(빈_테이블_등록_요청_결과);
                }),
                dynamicTest("손님 테이블을 등록요청하면 테이블이 등록된다.", () -> {
                    ResponseEntity<OrderTableResponse> 손님_테이블_등록_요청_결과 = 테이블_등록_요청(4, false);

                    테이블_등록됨(손님_테이블_등록_요청_결과);
                }),
                dynamicTest("테이블 목록을 조회요청하면 목록이 조회된다.", () -> {
                    ResponseEntity<List<OrderTableResponse>> 테이블_목록_조회_요청_결과 = 테이블_목록_조회_요청();

                    테이블_목록_조회됨(테이블_목록_조회_요청_결과);
                }),
                dynamicTest("빈 테이블 여부를 수정요청하면 테이블 정보가 수정된다.", () -> {
                    OrderTableResponse 빈_테이블 = 테이블_등록_되어있음(0, true);

                    ResponseEntity<OrderTableResponse> 빈_테이블_여부_수정_결과 = 테이블_빈_테이블_여부_수정_요청(빈_테이블, false);

                    테이블_빈_테이블_여부_수정됨(빈_테이블_여부_수정_결과, 빈_테이블);
                }),
                dynamicTest("손님수를 수정요청하면 테이블 정보가 수정된다.", () -> {
                    OrderTableResponse 손님_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<OrderTableResponse> 손님수_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, 5);

                    테이블_손님수_수정됨(손님수_수정_결과, 손님_테이블);
                })
        );
    }

    @TestFactory
    @DisplayName("테이블 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("손님 수를 0명 미만으로 수정할 수 없다.", () -> {
                    OrderTableResponse 손님_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<OrderTableResponse> 손님수_0명_미만_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, -1);

                    테이블_수정_실패됨(손님수_0명_미만_수정_결과);
                }),
                dynamicTest("존재하지 않는 테이블을 수정할 수 없다.", () -> {
                    OrderTableResponse 없는_테이블 = new OrderTableResponse(99L, 4, true);

                    ResponseEntity<OrderTableResponse> 없는_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(없는_테이블, 5);

                    테이블_수정_실패됨(없는_테이블_손님수_수정_결과);
                }),
                dynamicTest("빈 테이블은 손님 수를 수정할 수 없다.", () -> {
                    OrderTableResponse 빈_테이블 = 테이블_등록_되어있음(0, true);

                    ResponseEntity<OrderTableResponse> 빈_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(빈_테이블, 5);

                    테이블_수정_실패됨(빈_테이블_손님수_수정_결과);
                })
        );
    }

    @Test
    @DisplayName("주문이 조리 및 식사 중일때는 빈 테이블 여부를 변경할 수 없습니다.")
    void change() {
        OrderTableResponse 손님_테이블 = 테이블_등록_되어있음(4, false);
        MenuGroupResponse 추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
        ProductResponse 허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
        MenuResponse 허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 19_000L, 허니콤보, 1L);
        OrderResponse 주문 = 주문_등록_되어있음(손님_테이블, 1L, 허니레드콤보);

        ResponseEntity<OrderTableResponse> 테이블_빈_테이블_여부_수정_결과 = 테이블_빈_테이블_여부_수정_요청(손님_테이블, true);

        테이블_수정_실패됨(테이블_빈_테이블_여부_수정_결과);
    }

    public static OrderTableResponse 테이블_등록_되어있음(int numberOfGuests, boolean empty) {
        return 테이블_등록_요청(numberOfGuests, empty).getBody();
    }

    public static ResponseEntity<OrderTableResponse> 테이블_등록_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        return testRestTemplate.postForEntity("/api/tables", request, OrderTableResponse.class);
    }

    private void 테이블_수정_실패됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<OrderTableResponse> 테이블_손님수_수정_요청(OrderTableResponse orderTable, int numberOfGuests) {
        OrderTableRequest updateRequest = new OrderTableRequest(numberOfGuests, false);
        HttpEntity<OrderTableRequest> httpEntity = new HttpEntity<>(updateRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());

        return testRestTemplate.exchange("/api/tables/{orderTableId}/number-of-guests",
                HttpMethod.PUT,
                httpEntity,
                OrderTableResponse.class,
                params);
    }

    private void 테이블_손님수_수정됨(ResponseEntity<OrderTableResponse> response, OrderTableResponse expected) {
        OrderTableResponse actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getNumberOfGuests()).isNotEqualTo(expected.getNumberOfGuests());
    }

    private void 테이블_빈_테이블_여부_수정됨(ResponseEntity<OrderTableResponse> response, OrderTableResponse expected) {
        OrderTableResponse actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.isEmpty()).isNotEqualTo(expected.isEmpty());
    }

    public static ResponseEntity<OrderTableResponse> 테이블_빈_테이블_여부_수정_요청(OrderTableResponse orderTable, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(5, empty);
        HttpEntity<OrderTableRequest> httpEntity = new HttpEntity<>(orderTableRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());

        return testRestTemplate.exchange("/api/tables/{orderTableId}/empty",
                HttpMethod.PUT,
                httpEntity,
                OrderTableResponse.class,
                params);
    }

    private void 테이블_목록_조회됨(ResponseEntity<List<OrderTableResponse>> response, OrderTable... orderTables) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<Long> 테이블_목록_아이디_추출(Stream<OrderTableResponse> stream) {
        return stream
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<OrderTableResponse>> 테이블_목록_조회_요청() {
        return testRestTemplate.exchange("/api/tables", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTableResponse>>() {});
    }

    private void 테이블_등록됨(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }
}
