package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class tableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest orderTableRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTableRequest = new OrderTableRequest(0, true);
    }

    @DisplayName("DTO와 JPA를 사용하여 주문 테이블을 생성할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(orderTableRequest);

        //then
        정상_등록(response);
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    private ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables/temp")
                .then().log().all()
                .extract();
    }

}
