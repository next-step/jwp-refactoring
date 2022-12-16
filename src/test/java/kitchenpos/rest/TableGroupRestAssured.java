package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

import java.util.List;

public class TableGroupRestAssured {

    public static ExtractableResponse<Response> 주문_테이블_그룹_지정_요청(List<OrderTable> orderTables) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TableGroup(orderTables))
                .when().post(UriResource.주문_테이블_그룹_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_그룹_해지_요청(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(UriResource.주문_테이블_그룹_API.uri() + "/{tableGroupId}", id)
                .then().log().all()
                .extract();
    }
}
