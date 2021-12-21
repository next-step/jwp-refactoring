package kitchenpos.domain.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable savedOrderTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        savedOrderTable = orderTableDao.save(new OrderTable(null, 0, false));
    }

    @Test
    @DisplayName("테이블을 생성할 수 있다.")
    void createTable() {
        // given
        OrderTable 생성_할_테이블 = new OrderTable(0, true);

        // when
        ExtractableResponse<Response> 테이블_생성_요청_응답 = 테이블_생성_요청(생성_할_테이블);

        // then
        테이블_생성_됨(테이블_생성_요청_응답);
    }

    private void 테이블_생성_됨(ExtractableResponse<Response> response) {
        OrderTable 생성된_테이블 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(생성된_테이블.getId()).isNotNull(),
                () -> assertThat(생성된_테이블.getTableGroupId()).isNull(),
                () -> assertThat(생성된_테이블.getNumberOfGuests()).isZero(),
                () -> assertThat(생성된_테이블.isEmpty()).isTrue()
        );
    }

    public ExtractableResponse<Response> 테이블_생성_요청(OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("테이블의 사용여부를 변경할 수 있다.")
    void changeEmpty() {
        // given
        OrderTable 기존_테이블 = orderTableDao.save(new OrderTable(null, 0, true));
        OrderTable 기존_테이블_사용함_으로_변경 = new OrderTable(false);

        // when
        ExtractableResponse<Response> 테이블_사용여부_변경_요청_응답 = 테이블_사용여부_변경_요청(기존_테이블, 기존_테이블_사용함_으로_변경);

        // then
        테이블_사용여부_변경됨(테이블_사용여부_변경_요청_응답);
    }

    private void 테이블_사용여부_변경됨(ExtractableResponse<Response> response) {
        OrderTable 사용여부_변경된_테이블 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(사용여부_변경된_테이블.isEmpty()).isFalse()
        );

    }

    public ExtractableResponse<Response> 테이블_사용여부_변경_요청(OrderTable savedOrderTable, OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/empty", savedOrderTable.getId())
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // given
        OrderTable 변경할_손님_수 = new OrderTable(1);

        // when
        ExtractableResponse<Response> 방문한_손님_수_변경_응답 = 방문한_손님_수_변경_요청(변경할_손님_수);

        // then
        방문한_손님_수_변경됨(방문한_손님_수_변경_응답);
    }

    private void 방문한_손님_수_변경됨(ExtractableResponse<Response> response) {
        OrderTable 변경된_손님_수 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_손님_수.getNumberOfGuests()).isOne()
        );
    }

    public ExtractableResponse<Response> 방문한_손님_수_변경_요청(OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", savedOrderTable.getId())
                .then().log().all()
                .extract();
    }
}
