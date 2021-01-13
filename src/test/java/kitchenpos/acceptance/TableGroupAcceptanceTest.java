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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    private List<OrderTable> orderTables = new ArrayList<>();
    private TableGroup tableGroup;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTables.add(new OrderTable(1L));
        orderTables.add(new OrderTable(3L));
        tableGroup = new TableGroup(orderTables);

    }

    @DisplayName("테이블 그룹핑을 관리한다.")
    @Test
    void manageTableGrouping() {
        //등록
        ExtractableResponse<Response> createResponse = 테이블그룹핑_등록_요청(tableGroup);
        테이블그룹핑_등록됨(createResponse);

        //해제
        ExtractableResponse<Response> removeResponse = 테이블그룹핑_해제_요청(createResponse);
        테이블그룹핑_해제됨(removeResponse);
    }

    private ExtractableResponse<Response> 테이블그룹핑_등록_요청(TableGroup tableGroup) {
        return RestAssured.given().log().all().
                body(tableGroup).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/table-groups").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 테이블그룹핑_해제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().delete(uri).
                then().log().all().
                extract();
    }

    private void 테이블그룹핑_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(TableGroup.class).getOrderTables().get(0).getTableGroupId()).
                isEqualTo(response.as(TableGroup.class).getId());
        assertThat(response.as(TableGroup.class).getOrderTables().get(1).getTableGroupId()).
                isEqualTo(response.as(TableGroup.class).getId());
        assertThat(response.as(TableGroup.class).getCreatedDate()).isNotNull();
    }

    private void 테이블그룹핑_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
