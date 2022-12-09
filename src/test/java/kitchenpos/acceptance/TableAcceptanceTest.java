package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블_A;
    private OrderTable 주문테이블_B;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블_A = new OrderTable(null, 4,  false);
        주문테이블_B = new OrderTable(null, 4,  false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문테이블_생성_요청(주문테이블_A);

        // then
        주문테이블_생성됨(response);
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void findAllOrderTable() {
        // given
        주문테이블_A = 주문테이블_생성_요청(주문테이블_A).as(OrderTable.class);
        주문테이블_B = 주문테이블_생성_요청(주문테이블_B).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_목록_조회_요청();

        // then
        주문테이블_목록_조회됨(response, Arrays.asList(주문테이블_A.getId(), 주문테이블_B.getId()));
    }

    @DisplayName("주문 테이블이 비어있는 상태를 변경할 수 있다.")
    @Test
    void updateOrderTableEmpty() {
        // given
        boolean expectEmpty = false;
        주문테이블_A = 주문테이블_생성_요청(주문테이블_A).as(OrderTable.class);
        OrderTable 업데이트된_주문테이블_A = new OrderTable(
                주문테이블_A.getId(),
                주문테이블_A.getNumberOfGuests(),
                expectEmpty
        );

        // when
        ExtractableResponse<Response> response = 주문테이블_빈_여부_수정_요청(주문테이블_A.getId(), 업데이트된_주문테이블_A);

        // then
        주문테이블_빈_여부_수정됨(response, expectEmpty);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuest() {
        // given
        int expectedNumberOfGuest = 8;
        주문테이블_A = 주문테이블_생성_요청(주문테이블_A).as(OrderTable.class);
        OrderTable 업데이트된_주문테이블_A = new OrderTable(
                주문테이블_A.getId(),
                expectedNumberOfGuest,
                주문테이블_A.isEmpty()
        );

        // when
        ExtractableResponse<Response> response = 주문테이블_손님수_수정_요청(주문테이블_A.getId(), 업데이트된_주문테이블_A);

        // then
        주문테이블_손님수_수정됨(response, expectedNumberOfGuest);
    }

    public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문테이블_빈_여부_수정_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{id}/empty", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문테이블_손님수_수정_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{id}/number-of-guests", id)
                .then().log().all()
                .extract();
    }

    private void 주문테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문테이블_목록_조회됨(ExtractableResponse<Response> response, List<Long> tableIds) {
        List<Long> ids = response.jsonPath().getList(".", OrderTable.class)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        System.out.println(ids);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(tableIds)
        );
    }

    private void 주문테이블_빈_여부_수정됨(ExtractableResponse<Response> response, boolean expect) {
        boolean result = response.jsonPath().getBoolean("empty");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expect)
        );
    }

    private void 주문테이블_손님수_수정됨(ExtractableResponse<Response> response, int expect) {
        int result = response.jsonPath().getInt("numberOfGuests");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expect)
        );
    }
}
