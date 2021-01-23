package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관리")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블 그룹을 관리한다")
    @Test
    void manage() {
        TableGroupResponse tableGroup = 테이블_그룹_생성();
        테이블_그룹_삭제(tableGroup);
    }

    private TableGroupResponse 테이블_그룹_생성() {
        TableResponse table1 = TableAcceptanceTest.생성_요청()
                .as(TableResponse.class);
        TableResponse table2 = TableAcceptanceTest.생성_요청()
                .as(TableResponse.class);

        TableGroupRequest request = createRequest(table1, table2);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse);
        return createdResponse.as(TableGroupResponse.class);
    }

    private void 테이블_그룹_삭제(TableGroupResponse tableGroup) {
        ExtractableResponse<Response> deletedResponse = 삭제_요청(tableGroup.getId());

        삭제됨(deletedResponse);
    }

    public static TableGroupRequest createRequest(TableResponse... orderTables) {
        List<Long> tableIds = Stream.of(orderTables)
                .map(TableResponse::getId)
                .collect(Collectors.toList());
        return new TableGroupRequest(tableIds);
    }

    public static ExtractableResponse<Response> 생성_요청(TableGroupRequest request) {
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
