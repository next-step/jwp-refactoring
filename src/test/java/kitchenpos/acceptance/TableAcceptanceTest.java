package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 인수테스트 기능")
class TableAcceptanceTest extends AcceptanceTest {
    private static final String TABLE_URI = "/api/tables";

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    /**
     *  When 테이블 주문번호를 부여하면
     *  Then 주문번호가 부여된 테이블들을 조회할 수 있다.
     */
    @Test
    @DisplayName("테이블 주문번호를 부여하면 조회할 수 있다.")
    void createOrderTable() {
        // when
        final ExtractableResponse<Response> 테이블_주문_번호_생성_요청_결과 = 테이블_주문_번호_생성_요청(3, false);

        // then
        테이블_주문_번호_생성_확인(테이블_주문_번호_생성_요청_결과);
    }

    /**
     *  Given 테이블 주문번호가 주어지고
     *  When  빈 테이블로 변경하면
     *  Then  해당 주문번호는 빈 테이블로 변경된다.
     */
    @Test
    @DisplayName("빈 테이블로 변경하면 해당 주문번호는 빈 테이블로 변경된다.")
    void changeEmptyOrderTable() {
        // given
        테이블_주문_번호_생성_요청(3, false);

        // when
        final ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경(1);

        // then
        빈_테이블_확인(빈_테이블_변경_결과);
    }

    /**
     *  Given 테이블 주문번호가 주어지고
     *  When  방문 고객수를 변경하면
     *  Then  해당 주문번호는 고객수가 변경된다.
     */
    @Test
    @DisplayName("고객 수를 변경하면 해당 주문번호는 고객수가 변경된다.")
    void changeNumberOfGuests() {
        // given
        테이블_주문_번호_생성_요청(3, false);

        // when
        final ExtractableResponse<Response> 테이블_방문_고객수_변경_결과 = 테이블_방문_고객수_변경(1, 5);

        // then
        테이블_방문_고객수_확인(테이블_방문_고객수_변경_결과, 5);
    }

    public static ExtractableResponse<Response> 테이블_주문_번호_생성_요청(Integer 손님수, boolean 빈_테이블_유무) {
        final OrderTable 주문테이블 = new OrderTable(null, null, 손님수, 빈_테이블_유무);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문테이블)
                .when().post(TABLE_URI)
                .then().log().all()
                .extract();
    }

    public static void 테이블_주문_번호_생성_확인(ExtractableResponse<Response> 테이블_주문_번호_생성_요청_결과) {
        assertThat(테이블_주문_번호_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 빈_테이블_변경(Integer 변경할_주문_테이블_번호) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(null, null, 0, true))
                .when().put(TABLE_URI + "/{orderTableId}/empty", 변경할_주문_테이블_번호)
                .then().log().all()
                .extract();
    }

    public void 빈_테이블_확인(ExtractableResponse<Response> 빈_테이블_변경_결과) {
        final OrderTable 빈_테이블 = 빈_테이블_변경_결과.body().jsonPath().getObject(".", OrderTable.class);

        assertAll(
                () -> assertThat(빈_테이블_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(빈_테이블.isEmpty()).isTrue()
        );
    }

    public static ExtractableResponse<Response> 테이블_방문_고객수_변경(Integer 변경할_주문_테이블_번호, Integer 변경할_고객_수) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTable(null, null, 변경할_고객_수, false))
                .when().put(TABLE_URI + "/{orderTableId}/number-of-guests", 변경할_주문_테이블_번호)
                .then().log().all()
                .extract();
    }

    public static void 테이블_방문_고객수_확인(ExtractableResponse<Response> 테이블_방문_고객수_변경_결과, Integer 예상된_변경_고객수) {
        final OrderTable 변경된_고객_테이블 = 테이블_방문_고객수_변경_결과.body().jsonPath().getObject(".", OrderTable.class);

        assertAll(
                () -> assertThat(테이블_방문_고객수_변경_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_고객_테이블.getNumberOfGuests()).isEqualTo(예상된_변경_고객수)
        );
    }
}
