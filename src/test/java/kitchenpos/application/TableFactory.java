package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class TableFactory {

    public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTableRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문테이블_상태변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/"+orderTableId+"/empty")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문테이블_게스트변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/"+orderTableId+"/number-of-guests")
                .then().log().all().
                extract();
    }

    public static OrderTableResponse 주문테이블이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderTableResponse.class);
    }
}
