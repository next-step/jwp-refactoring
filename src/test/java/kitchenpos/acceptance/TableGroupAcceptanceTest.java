package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();

        firstOrderTable = TableAcceptanceTest.주문_테이블_등록_되어있음(new OrderTable(50, true)).as(OrderTable.class);
        secondOrderTable = TableAcceptanceTest.주문_테이블_등록_되어있음(new OrderTable(30, true)).as(OrderTable.class);
    }

    @DisplayName("단체 지정을 관리한다.")
    @Test
    void manageTableGroup() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(tableGroup);

        // then
        단체_지정_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);

        // then
        단체_지정_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 단체_지정_생성_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private void 단체_지정_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 단체_지정_삭제_요청(ExtractableResponse<Response> createResponse) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private void 단체_지정_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
