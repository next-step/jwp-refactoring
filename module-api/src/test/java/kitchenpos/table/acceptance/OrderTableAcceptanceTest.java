package kitchenpos.table.acceptance;

import static kitchenpos.utils.StatusValidation.변경됨;
import static kitchenpos.utils.StatusValidation.생성됨;
import static kitchenpos.utils.TestFactory.post;
import static kitchenpos.utils.TestFactory.put;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.moduledomain.table.NumberOfGuests;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    private final static String TABLE_BASE_URL = "/tables";
    private OrderTableRequest 주문_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문_테이블 = new OrderTableRequest(new NumberOfGuests(0), false);
    }

    @Test
    void 주문테이블을_생성한다() {
        // when
        ExtractableResponse<Response> response = 주문테이블_생성_요청(주문_테이블);

        // then
        주문테이블_생성됨(response);
    }

    @Test
    void 주문테이블_을_빈_테이블로_변경한다() {

        // given
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);
        OrderTableResponse orderTableResponse = 주문테이블_생성_요청(주문_테이블).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_빈테이블_변경_요청(orderTableResponse.getId(),
            changeEmptyRequest);

        // then
        주문테이블_변경_됨(response);

    }

    @Test
    void 주문테이블에_손님_수를_변경한다() {
        // given
        OrderTableResponse orderTableResponse = 주문테이블_생성_요청(주문_테이블).as(OrderTableResponse.class);
        ChangeNumberOfGuestRequest changeNumberOfGuestRequest = new ChangeNumberOfGuestRequest(3);

        // when
        ExtractableResponse<Response> response = 주문테이블_손님_수_변경_요청(
            orderTableResponse.getId(), changeNumberOfGuestRequest);

        // then
        주문테이블_손님_수_변경됨(response);
    }


    public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return post(TABLE_BASE_URL, orderTableRequest);
    }

    public static void 주문테이블_생성됨(ExtractableResponse<Response> response) {
        생성됨(response);
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블_변경_요청(Long id,
        ChangeEmptyRequest changeEmptyRequest) {
        return put(TABLE_BASE_URL + "/{orderTableId}/empty", "orderTableId", id,
            changeEmptyRequest);
    }

    public static void 주문테이블_변경_됨(ExtractableResponse<Response> response) {
        변경됨(response);
    }

    public static ExtractableResponse<Response> 주문테이블_손님_수_변경_요청(Long id,
        ChangeNumberOfGuestRequest changeNumberOfGuestRequest) {
        return put(TABLE_BASE_URL + "/{orderTableId}/number-of-guests", "orderTableId", id,
            changeNumberOfGuestRequest);
    }

    public static void 주문테이블_손님_수_변경됨(ExtractableResponse<Response> response) {
        변경됨(response);
    }


}
