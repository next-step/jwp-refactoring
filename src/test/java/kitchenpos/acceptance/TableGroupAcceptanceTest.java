package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관리")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블 그룹을 관리한다")
    @Test
    void manage() {
        //given
        OrderTable table1 = TableAcceptanceTest.생성_요청(TableAcceptanceTest.createRequest())
                .as(OrderTable.class);
        OrderTable table2 = TableAcceptanceTest.생성_요청(TableAcceptanceTest.createRequest())
                .as(OrderTable.class);
        //when
        TableGroup request = createRequest(table1, table2);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);
        //then
        생성됨(createdResponse);
        //when
        TableGroup tableGroup = createdResponse.as(TableGroup.class);
        ExtractableResponse<Response> deletedResponse = 삭제_요청(tableGroup.getId());
        //then
        삭제됨(deletedResponse);
    }

    public static TableGroup createRequest(OrderTable ... tables) {
        TableGroup request = new TableGroup();
        request.setOrderTables(Arrays.asList(tables));
        return request;
    }

    public static ExtractableResponse<Response> 생성_요청(TableGroup request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 삭제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
