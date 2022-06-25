package kitchenpos.acceptance;

import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTable = createOrderTable(1L, null, 5, false);
    }

    /**
     * Feature: 테이블을 관리한다.
     * <p>
     * Scenario: 테이블 관리
     * <p>
     * When 주문 테이블 등록 요청
     * <p>
     * Then 주문 테이블 등록됨
     * <p>
     * When 주문 테이블 목록 조회 요청
     * <p>
     * Then 주문 테이블 목록이 조회됨
     * <p>
     * When 주문 테이블을 빈 테이블로 변경 요청
     * <p>
     * Then 주문 테이블이 빈테이블로 변경됨
     * <p>
     * When 빈 테이블을 주문 테이블로 변경 요청
     * <p>
     * Then 빈 테이블이 주문 테이블로 변경됨
     * <p>
     * When 테이블의 손님 수를 변경 요청
     * <p>
     * Then 테이블의 손님 수가 변경됨
     */
    @Test
    void 테이블_관리() {
        ExtractableResponse<Response> response;
        // when 주문테이블 등록 요청
        response = 주문테이블_등록_요청(orderTable);
        // then 주문테이블 등록됨
        주문테이블_등록됨(response);

        // when 주문테이블 목록 조회 요청
        response = 주문테이블_목록_조회();
        // then 주문테이블 목록이 조회됨
        주문테이블_목록_조회됨(response);
        // then 주문테이블 목록이 조회됨
        주문테이블_목록_포함됨(response, Arrays.asList(orderTable));

        // when 주문 테이블을 빈 테이블로 변경
        response = 주문테이블_빈_테이블로_변경_요청(orderTable);
        // then 주문 테이블이 빈 테이블로 변경됨
        주문테이블_빈_테이블로_변경됨(response);

        // when 주문 테이블을 빈 테이블로 변경
        response = 빈_테이블_주문_테이블로_변경_요청(orderTable);
        // then 주문 테이블이 빈 테이블로 변경됨
        빈_테이블_주문_테이블로_변경됨(response);

        // when 테이블의 손님 수 변경을 요청
        int numberOfGuests = 10;
        orderTable.setNumberOfGuests(numberOfGuests);
        response = 테이블_손님_수_변경_요청(orderTable);
        // then 테이블의 손님 수가 변경됨
        테이블_손님_수_변경됨(response, orderTable);
    }

    public static ExtractableResponse<Response> 주문테이블_등록_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_빈_테이블로_변경_요청(OrderTable orderTable) {
        orderTable.setEmpty(true);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 빈_테이블_주문_테이블로_변경_요청(OrderTable orderTable) {
        orderTable.setEmpty(false);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님_수_변경_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문테이블_목록_포함됨(ExtractableResponse<Response> response, List<OrderTable> expectedOrderTables) {
        List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<Long> expectedOrderTableIds = expectedOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
    }

    public static void 주문테이블_빈_테이블로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(true);
    }

    public static void 빈_테이블_주문_테이블로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(false);
    }

    private static void 테이블_손님_수_변경됨(ExtractableResponse<Response> response, OrderTable orderTable) {
        assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
