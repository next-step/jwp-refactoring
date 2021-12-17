package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.빈테이블_등록됨;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_목록_조회_요청;
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
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/table-groups";

    @Test
    @DisplayName("단체를 지정/해제 할 수 있다.")
    void manageTableGroup() {
        // 빈 테이블 등록되어 있음
        OrderTable 테이블_1 = 빈테이블_등록됨();
        OrderTable 테이블_2 = 빈테이블_등록됨();
        List<OrderTable> orderTables = Arrays.asList(테이블_1, 테이블_2);

        // 단체지정 요청
        ExtractableResponse<Response> saveResponse = 단체지정_요청(orderTables);
        // 단체지정 됨
        단체지정됨(saveResponse, orderTables);

        // 테이블 목록 조회 요청
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();
        // 테이블 목록 조회됨
        테이블_단체지정_확인(response.jsonPath().getList(".", OrderTable.class));

        // 단체지정 해제(삭제)
        ExtractableResponse<Response> deleteResponse = 단제지정_해제_요청(saveResponse.jsonPath().getLong("id"));
        // 단제지정 해제됨
        단체지정_해제됨(deleteResponse);

        // 테이블 목록 조회 요청
        ExtractableResponse<Response> tableResponse = 테이블_목록_조회_요청();
        // 테이블 목록 조회됨
        테이블_단체지정_해제_확인(tableResponse.jsonPath().getList(".", OrderTable.class));

    }

    public static ExtractableResponse<Response> 단체지정_요청(List<OrderTable> tables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tables);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 단체지정됨(ExtractableResponse<Response> response, List<OrderTable> expectedTables) {
        TableGroup tableGroup = response.as(TableGroup.class);
        List<Long> expectedIds = expectedTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(tableGroup.getOrderTables())
                .extracting(OrderTable::getId)
                .containsAll(expectedIds);
            assertNotNull(tableGroup.getCreatedDate());
        });
    }

    public static ExtractableResponse<Response> 단제지정_해제_요청(Long groupId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(URL+"/{tableGroupId}", groupId)
            .then().log().all()
            .extract();
    }

    public static void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 테이블_단체지정_확인(List<OrderTable> tables) {
        long count = tables.stream()
            .filter(it -> it.getTableGroupId() == null)
            .count();

        assertThat(count).isEqualTo(0);
    }

    private void 테이블_단체지정_해제_확인(List<OrderTable> tables) {
        long count = tables.stream()
            .filter(it -> it.getTableGroupId() != null)
            .count();

        assertThat(count).isEqualTo(0);
    }
}
