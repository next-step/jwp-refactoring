package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.응답코드_확인;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    @DisplayName("단체 지정을 관리한다")
    @Test
    public void manageTableGroup() {
        //단체 지정 생성
        //given
        TableGroup 단체지정 = new TableGroup(1l, createOrderTables());
        //when
        ExtractableResponse<Response> 단체_지정_요청 = 단체_지정_요청(단체지정);
        //then
        응답코드_확인(단체_지정_요청, HttpStatus.CREATED);

        //단체 지정 해제
        //when
        ExtractableResponse<Response> 단체_지정_해제_요청 = 단체_지정_해제_요청(단체_지정_요청.as(TableGroup.class).getId());
        //then
        응답코드_확인(단체_지정_해제_요청, HttpStatus.NO_CONTENT);

    }

    public static ExtractableResponse<Response> 단체_지정_요청(TableGroup tableGroup) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
            .then().log().all()
            .extract();
    }

    private List<OrderTable> createOrderTables() {
        return Arrays.asList(new OrderTable(3l,  1, true), new OrderTable(4l,  1, true));
    }


}
