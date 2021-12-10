package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableServiceAcceptanceTest extends AcceptanceTest {


    @DisplayName("주문테이블을 등록한다")
    @Test
    void createTest() {

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTable);
        OrderTable createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);
        ExtractableResponse<Response> getResponse = TableFactory.주문테이블_조회_요청();
        List<OrderTable> tableFactories = Arrays.asList(getResponse.as(OrderTable[].class));
        assertThat(tableFactories).contains(createdOrderTable);
    }

    @DisplayName("주문테이블 상태를 변경한다.")
    @Test
    void changeEmptyTest() {

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTable);
        OrderTable createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);
        createdOrderTable.changeFull();

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_상태변경_요청(createdOrderTable.getId(), createdOrderTable);
        assertThat(changeStatusResponse.as(OrderTable.class).isEmpty()).isFalse();
    }

    @DisplayName("주문테이블 게스트 수를 변경한다.")
    @Test
    void changeNumberOfGuestsTest() {

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTable);
        OrderTable createdOrderTable = TableFactory.주문테이블이_생성됨(createResponse);
        createdOrderTable.setNumberOfGuests(3);

        ExtractableResponse<Response> changeStatusResponse = TableFactory.주문테이블_게스트변경_요청(createdOrderTable.getId(), createdOrderTable);
        assertThat(changeStatusResponse.as(OrderTable.class).getNumberOfGuests()).isEqualTo(3);
    }

    
}
