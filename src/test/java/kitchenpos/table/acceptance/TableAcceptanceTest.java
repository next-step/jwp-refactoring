package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private static final String URL = "/api/tables";

    public static ExtractableResponse<Response> 테이블_등록_요청(int numberOfGuests, boolean isEmpty) {
        OrderTableRequest orderTable = new OrderTableRequest(numberOfGuests, isEmpty);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 빈_테이블_등록_요청() {
        return 테이블_등록_요청(0, true);
    }

    public static void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 빈_테이블_등록됨(ExtractableResponse<Response> response) {
        OrderTableResponse orderTable = response.as(OrderTableResponse.class);

        assertAll(() -> {
            테이블_등록됨(response);
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
            assertTrue(orderTable.getEmpty());
        });
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 테이블_목록_조회됨(ExtractableResponse<Response> response,
        List<OrderTableResponse> expected) {
        List<OrderTableResponse> orderTables = response.jsonPath()
            .getList(".", OrderTableResponse.class);
        List<Long> expectedIds = expected.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTables)
                .extracting(OrderTableResponse::getId)
                .containsAll(expectedIds);
        });
    }

    public static ExtractableResponse<Response> 테이블_상태변경_요청(Long tableId, boolean isEmpty) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("empty", isEmpty)
            .when().put(URL + "/{orderTableId}/empty", tableId)
            .then().log().all()
            .extract();
    }

    public static void 테이블_상태변경_됨(ExtractableResponse<Response> response, boolean expected) {
        OrderTableResponse orderTable = response.as(OrderTableResponse.class);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTable.getEmpty()).isEqualTo(expected);
        });
    }

    public static ExtractableResponse<Response> 테이블_손님수_변경_요청(Long tableId, int numberOfGuests) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("numberOfGuests", numberOfGuests)
            .when().put(URL + "/{orderTableId}/number-of-guests", tableId)
            .then().log().all()
            .extract();
    }

    public static void 테이블_손님수_변경_됨(ExtractableResponse<Response> response, int expected) {
        OrderTableResponse orderTable = response.as(OrderTableResponse.class);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
        });
    }

    public static OrderTableResponse 빈테이블_등록됨() {
        return 빈_테이블_등록_요청().as(OrderTableResponse.class);
    }

    public static OrderTableResponse 주문테이블_등록되어있음(int numberOfGuests) {
        return 테이블_등록_요청(numberOfGuests, false).as(OrderTableResponse.class);
    }

    @DisplayName("테이블을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageTable() {
        return Stream.of(
            dynamicTest("빈 테이블을 등록한다.", () -> {
                // 빈 테이블 등록 요청
                ExtractableResponse<Response> saveResponse = 빈_테이블_등록_요청();
                // 빈 테이블 등록 됨
                빈_테이블_등록됨(saveResponse);

                // 테이블 목록 조회 요청
                ExtractableResponse<Response> response = 테이블_목록_조회_요청();
                // 테이블 목록 조회됨
                테이블_목록_조회됨(response, Arrays.asList(saveResponse.as(OrderTableResponse.class)));
            }),
            dynamicTest("테이블의 상태를 변경한다.", () -> {
                // 테이블 목록 조회 요청
                ExtractableResponse<Response> response = 테이블_목록_조회_요청();
                List<OrderTableResponse> orderTables = response.jsonPath()
                    .getList(".", OrderTableResponse.class);
                OrderTableResponse orderTable = orderTables.get(0);

                // 빈테이블 -> 주문 테이블 상태 변경 요청
                ExtractableResponse<Response> emptyResponse = 테이블_상태변경_요청(orderTable.getId(),
                    false);

                // 상태 변경됨
                테이블_상태변경_됨(emptyResponse, false);

                // 테이블의 손님 수 변경 요청
                ExtractableResponse<Response> numberResponse = 테이블_손님수_변경_요청(orderTable.getId(), 3);

                // 테이블의 손님 수 변경 됨
                테이블_손님수_변경_됨(numberResponse, 3);

                // 테이블 목록 조회 요청
                response = 테이블_목록_조회_요청();
                // 테이블 목록 조회됨
                테이블_목록_조회됨(response, Arrays.asList(numberResponse.as(OrderTableResponse.class)));
            })
        );
    }

}
