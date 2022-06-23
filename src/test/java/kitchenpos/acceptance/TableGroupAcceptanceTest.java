package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.TableGroupServiceTest;
import kitchenpos.application.TableServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    TableGroup tableGroup;

    @BeforeEach
    public void init() {
        super.init();

        OrderTable orderTable = TableServiceTest.주문_테이블_생성(null, null, true, 0);
        OrderTable 주문_테이블1 = TableAcceptanceTest.주문_테이블_생성_요청(orderTable).as(OrderTable.class);
        OrderTable 주문_테이블2 = TableAcceptanceTest.주문_테이블_생성_요청(orderTable).as(OrderTable.class);
        tableGroup = TableGroupServiceTest.테이블_단체_지정(null, 주문_테이블1, 주문_테이블2);
    }

    @DisplayName("주문 테이블들을 단체 지정한다.")
    @Test
    void 주문_테이블들_단체_지정() {
        // when
        ExtractableResponse<Response> response = 주문_테이블들_단체_지정_요청(tableGroup);

        // then
        단체_지정됨(response);
    }

    @DisplayName("주문 테이블 단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블들_단체_지정_요청(tableGroup);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(createResponse);

        // then
        단체_지정_해제됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블들_단체_지정_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
