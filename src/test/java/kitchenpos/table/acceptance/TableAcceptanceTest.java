package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;

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
                    ResponseEntity<OrderTable>빈_테이블_등록_요청_결과 = 테이블_등록_요청(0, true);

                    테이블_등록됨(빈_테이블_등록_요청_결과);
                }),
                dynamicTest("손님 테이블을 등록요청하면 테이블이 등록된다.", () -> {
                    ResponseEntity<OrderTable> 손님_테이블_등록_요청_결과 = 테이블_등록_요청(4, false);

                    테이블_등록됨(손님_테이블_등록_요청_결과);
                }),
                dynamicTest("테이블 목록을 조회요청하면 목록이 조회된다.", () -> {
                    ResponseEntity<List<OrderTable>> 테이블_목록_조회_요청_결과 = 테이블_목록_조회_요청();

                    테이블_목록_조회됨(테이블_목록_조회_요청_결과);
                }),
                dynamicTest("빈 테이블 여부를 수정요청하면 테이블 정보가 수정된다.", () -> {
                    OrderTable 빈_테이블 = 테이블_등록_되어있음(0, true);

                    ResponseEntity<OrderTable> 빈_테이블_여부_수정_결과 = 테이블_빈_테이블_여부_수정_요청(빈_테이블, false);

                    테이블_빈_테이블_여부_수정됨(빈_테이블_여부_수정_결과, 빈_테이블);
                }),
                dynamicTest("손님수를 수정요청하면 테이블 정보가 수정된다.", () -> {
                    OrderTable 손님_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<OrderTable> 손님수_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, 5);

                    테이블_손님수_수정됨(손님수_수정_결과, 손님_테이블);
                })
        );
    }

    @TestFactory
    @DisplayName("테이블 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("손님 수를 0명 미만으로 수정할 수 없다.", () -> {
                    OrderTable 손님_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<OrderTable> 손님수_0명_미만_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, -1);

                    테이블_손님수_수정_실패됨(손님수_0명_미만_수정_결과);
                }),
                dynamicTest("존재하지 않는 테이블을 수정할 수 없다.", () -> {
                    OrderTable 없는_테이블 = new OrderTable();
                    없는_테이블.setId(99L);

                    ResponseEntity<OrderTable> 없는_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(없는_테이블, 5);

                    테이블_손님수_수정_실패됨(없는_테이블_손님수_수정_결과);
                }),
                dynamicTest("빈 테이블은 손님 수를 수정할 수 없다.", () -> {
                    OrderTable 빈_테이블 = 테이블_등록_되어있음(0, true);

                    ResponseEntity<OrderTable> 빈_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(빈_테이블, 5);

                    테이블_손님수_수정_실패됨(빈_테이블_손님수_수정_결과);
                })
        );
    }

    public static OrderTable 테이블_등록_되어있음(int numberOfGuests, boolean empty) {
        return 테이블_등록_요청(numberOfGuests, empty).getBody();
    }

    public static ResponseEntity<OrderTable> 테이블_등록_요청(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);

        return testRestTemplate.postForEntity("/api/tables", orderTable, OrderTable.class);
    }

    private void 테이블_손님수_수정_실패됨(ResponseEntity<OrderTable> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<OrderTable> 테이블_손님수_수정_요청(OrderTable orderTable, int numberOfGuests) {
        OrderTable updateRequest = new OrderTable();
        updateRequest.setNumberOfGuests(numberOfGuests);
        HttpEntity<OrderTable> httpEntity = new HttpEntity<>(updateRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());

        return testRestTemplate.exchange("/api/tables/{orderTableId}/number-of-guests",
                HttpMethod.PUT,
                httpEntity,
                OrderTable.class,
                params);
    }

    private void 테이블_손님수_수정됨(ResponseEntity<OrderTable> response, OrderTable expected) {
        OrderTable actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getNumberOfGuests()).isNotEqualTo(expected.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    private void 테이블_빈_테이블_여부_수정됨(ResponseEntity<OrderTable> response, OrderTable expected) {
        OrderTable actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(actual.isEmpty()).isNotEqualTo(expected.isEmpty());
    }

    private ResponseEntity<OrderTable> 테이블_빈_테이블_여부_수정_요청(OrderTable orderTable, boolean empty) {
        OrderTable updateRequest = new OrderTable();
        updateRequest.setEmpty(empty);
        HttpEntity<OrderTable> httpEntity = new HttpEntity<>(updateRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());

        return testRestTemplate.exchange("/api/tables/{orderTableId}/empty",
                HttpMethod.PUT,
                httpEntity,
                OrderTable.class,
                params);
    }

    private void 테이블_목록_조회됨(ResponseEntity<List<OrderTable>> response, OrderTable... orderTables) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<Long> 테이블_목록_아이디_추출(Stream<OrderTable> stream) {
        return stream
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<OrderTable>> 테이블_목록_조회_요청() {
        return testRestTemplate.exchange("/api/tables", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTable>>() {});
    }

    private void 테이블_등록됨(ResponseEntity<OrderTable> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }
}
