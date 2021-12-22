package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.빈테이블_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String URL = "/api/table-groups";

    public static ExtractableResponse<Response> 단체지정_요청(List<OrderTableResponse> tables) {
        List<Long> ids = tables.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(ids)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 단체지정됨(ExtractableResponse<Response> response,
        List<OrderTableResponse> expectedTables) {
        TableGroupResponse tableGroup = response.as(TableGroupResponse.class);
        List<Long> expectedIds = expectedTables.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(tableGroup.getOrderTables())
                .extracting(OrderTableResponse::getId)
                .containsAll(expectedIds);
            assertNotNull(tableGroup.getCreatedDate());
        });
    }

    public static ExtractableResponse<Response> 단제지정_해제_요청(Long groupId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(URL + "/{tableGroupId}", groupId)
            .then().log().all()
            .extract();
    }

    public static void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("단체를 지정/해제 할 수 있다.")
    void manageTableGroup() {
        // 빈 테이블 등록되어 있음
        OrderTableResponse 테이블_1 = 빈테이블_등록됨();
        OrderTableResponse 테이블_2 = 빈테이블_등록됨();
        List<OrderTableResponse> orderTables = Arrays.asList(테이블_1, 테이블_2);

        // 단체지정 요청
        ExtractableResponse<Response> saveResponse = 단체지정_요청(orderTables);
        // 단체지정 됨
        단체지정됨(saveResponse, orderTables);

        // 단체지정 해제(삭제)
        ExtractableResponse<Response> deleteResponse = 단제지정_해제_요청(
            saveResponse.jsonPath().getLong("id"));
        // 단제지정 해제됨
        단체지정_해제됨(deleteResponse);

    }

}
