package kitchenpos.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class TableServiceAcceptanceTest extends AcceptanceTest {


    @DisplayName("주문테이블을 등록한다")
    @Test
    void createTest() {

        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTableRequest);
        OrderTableResponse createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);
        ExtractableResponse<Response> getResponse = TableFactory.주문테이블_조회_요청();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @DisplayName("주문테이블 상태를 변경한다.")
    @Test
    void changeEmptyTest() {

        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTableRequest);
        OrderTableResponse createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_상태변경_요청(
                                                                                createdOrderTable.getId(),
                                                                                new OrderTableRequest(createdOrderTable.getNumberOfGuests(), true));
        assertThat(changeStatusResponse.as(OrderTableResponse.class).isEmpty()).isTrue();
    }

    @DisplayName("주문테이블 게스트 수를 변경한다.")
    @Test
    void changeNumberOfGuestsTest() {

        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTableRequest);
        OrderTableResponse createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_게스트변경_요청(
                                                                        createdOrderTable.getId(),
                                                                        new OrderTableRequest(3, createdOrderTable.isEmpty()));
        assertThat(changeStatusResponse.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(3);
    }

    
}
