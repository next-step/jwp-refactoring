package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class TableServiceAcceptanceTest extends AcceptanceTest {


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

        OrderTable orderTable = new OrderTable(createdOrderTable.getId(), createdOrderTable.getNumberOfGuests(), createdOrderTable.isEmpty());
        orderTable.changeNonEmptyOrderTable();

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_상태변경_요청(
                                                                                createdOrderTable.getId(),
                                                                                new OrderTableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        assertThat(changeStatusResponse.as(OrderTable.class).isEmpty()).isFalse();
    }

    @DisplayName("주문테이블 게스트 수를 변경한다.")
    @Test
    void changeNumberOfGuestsTest() {

        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTableRequest);
        OrderTableResponse createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);
        OrderTable orderTable = new OrderTable(createdOrderTable.getId(), createdOrderTable.getNumberOfGuests(), createdOrderTable.isEmpty());
        orderTable.changeNumberOfGuests(3);

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_게스트변경_요청(
                                                                        createdOrderTable.getId(),
                                                                        new OrderTableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        assertThat(changeStatusResponse.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(3);
    }

    
}
