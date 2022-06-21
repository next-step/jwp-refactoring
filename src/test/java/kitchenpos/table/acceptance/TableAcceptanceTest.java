package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@DisplayName("테이블 관련 기능 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    /**
     * Feature 테이블 관련 기능
     *
     * Scenario 테이블 관련 기능
     * When 테이블 등록 요청
     * Then 테이블 등록됨
     * When 테이블 목록 조회 요청
     * Then 테이블 목록 조회됨
     *
     * When 테이블 빈 테이블 여부 수정 요청
     * Then 테이블 빈 테이블 여부 수정됨
     *
     * When 테이블 0명 미만 손님수 수정 요청
     * Then 테이블 수정 실패됨
     * When 없는 테이블 손님수 수정 요청
     * Then 테이블 수정 실패됨
     * When 빈 테이블 손님수 수정 요청
     * Then 테이블 수정 실패됨
     *
     * When 테이블 손님수 수정 요청
     * Then 테이블 손님수 수정됨
     */
    @Test
    @DisplayName("테이블 관련 기능")
    void integrationTest() {
        //when
        boolean 비어있음 = true;
        ResponseEntity<OrderTable>빈_테이블_등록_요청_결과 = 테이블_등록_요청(0, 비어있음);
        OrderTable 빈_테이블 = 빈_테이블_등록_요청_결과.getBody();
        //then
        테이블_등록됨(빈_테이블_등록_요청_결과);

        //when
        boolean 비어있지않음 = false;
        ResponseEntity<OrderTable> 손님_테이블_등록_요청_결과 = 테이블_등록_요청(4, 비어있지않음);
        OrderTable 손님_테이블 = 손님_테이블_등록_요청_결과.getBody();
        //then
        테이블_등록됨(손님_테이블_등록_요청_결과);

        //when
        ResponseEntity<List<OrderTable>> 테이블_목록_조회_요청_결과 = 테이블_목록_조회_요청();
        //then
        테이블_목록_조회됨(테이블_목록_조회_요청_결과, 빈_테이블, 손님_테이블);

        //when
        ResponseEntity<OrderTable> 빈_테이블_여부_수정_결과 = 테이블_빈_테이블_여부_수정_요청(빈_테이블, 비어있지않음);
        //then
        테이블_빈_테이블_여부_수정됨(빈_테이블_여부_수정_결과, 빈_테이블);

        //when
        ResponseEntity<OrderTable> 손님수_0명_미만_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, -1);
        //then
        테이블_손님수_수정_실패됨(손님수_0명_미만_수정_결과);

        //when
        OrderTable 없는_테이블 = new OrderTable();
        없는_테이블.setId(99L);
        ResponseEntity<OrderTable> 없는_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(없는_테이블, 5);
        //then
        테이블_손님수_수정_실패됨(없는_테이블_손님수_수정_결과);

        //when
        테이블_빈_테이블_여부_수정_요청(빈_테이블, 비어있음);
        ResponseEntity<OrderTable> 빈_테이블_손님수_수정_결과 = 테이블_손님수_수정_요청(빈_테이블, 5);
        //then
        테이블_손님수_수정_실패됨(빈_테이블_손님수_수정_결과);

        //when
        ResponseEntity<OrderTable> 손님수_수정_결과 = 테이블_손님수_수정_요청(손님_테이블, 5);
        //then
        테이블_손님수_수정됨(손님수_수정_결과, 손님_테이블);
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
        List<Long> actualIds = 테이블_목록_아이디_추출(response.getBody().stream());
        List<Long> expectedIds = 테이블_목록_아이디_추출(Arrays.stream(orderTables));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
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
