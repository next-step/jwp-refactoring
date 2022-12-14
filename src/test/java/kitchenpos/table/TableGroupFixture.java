package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.http.MediaType;

public class TableGroupFixture {

    public static ExtractableResponse<Response> 단체_지정(List<OrderTable> orderTAbles) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTAbles);

        return RestAssured
            .given().log().all()
            .body(tableGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제(long tableGroupId) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/table-groups/" + tableGroupId)
            .then().log().all()
            .extract();
    }
}
