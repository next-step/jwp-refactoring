package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.http.MediaType;

public class TableGroupApiHelper {

    public static ExtractableResponse<Response> 단체_테이블_등록하기(List<OrderTable> 테이블_정보들) {
        TableGroup 테이블그룹 = new TableGroup();
        테이블그룹.setOrderTables(테이블_정보들);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(테이블그룹)
            .when().post("/api/table-groups")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 단체_테이블_삭제하기(long 테이블번호) {
        return RestAssured
            .given().log().all()
            .when().delete("/api/table-groups/" + 테이블번호)
            .then().log().all().
            extract();
    }

}
