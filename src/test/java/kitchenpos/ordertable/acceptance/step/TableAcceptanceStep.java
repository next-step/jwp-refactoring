package kitchenpos.ordertable.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.step.HttpUtil;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

public class TableAcceptanceStep {

    private static final String API_URL = "/api/tables";

    private TableAcceptanceStep() {
    }

    public static Long 주문테이블_생성됨(int numberOfGuests, boolean empty) {
        return 주문테이블_등록_검증(주문테이블_등록_요청(numberOfGuests, empty));
    }

    public static ExtractableResponse<Response> 주문테이블_등록_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest 요청_데이터 = OrderTableRequest.of(numberOfGuests, empty);
        return HttpUtil.post(API_URL, 요청_데이터);
    }

    public static ExtractableResponse<Response> 주문테이블_목록조회() {
        return HttpUtil.get(API_URL);
    }


    public static ExtractableResponse<Response> 주문테이블_빈테이블_상태_변경_요청(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        String url = API_URL + "/" + orderTableId + "/empty";
        return HttpUtil.put(url, orderTableRequest);
    }


    public static ExtractableResponse<Response> 주문테이블_방문손님수_변경_요청(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        String url = API_URL + "/" + orderTableId + "/number-of-guests";
        return HttpUtil.put(url, orderTableRequest);
    }

    public static Long 주문테이블_등록_검증(ExtractableResponse<Response> response) {
        OrderTableResponse 등록된_주문테이블 = response.as(OrderTableResponse.class);

        assertAll(
            () -> assertThat(등록된_주문테이블.getId()).isNotNull(),
            () -> assertThat(등록된_주문테이블.getTableGroupId()).isNull()
        );
        return 등록된_주문테이블.getId();
    }


    public static OrderTableResponse 빈테이블_변경_검증(ExtractableResponse<Response> response,
        OrderTableRequest expected) {
        OrderTableResponse 변경된_테이블 = response.as(OrderTableResponse.class);

        assertThat(변경된_테이블.isEmpty()).isEqualTo(expected.isEmpty());

        return 변경된_테이블;
    }

    public static void 주문테이블_목록조회_검증(ExtractableResponse<Response> response, Long orderTableId) {
        List<OrderTableResponse> 조회된_메뉴그룹_목록 = response.as(new TypeRef<List<OrderTableResponse>>() {
        });

        assertThat(조회된_메뉴그룹_목록).extracting("id").contains(orderTableId);
    }

    public static void 방문한_손님_수_변경_검증(ExtractableResponse<Response> response,
        OrderTableRequest expected) {
        OrderTableResponse 주문테이블_결과 = response.as(OrderTableResponse.class);

        assertThat(주문테이블_결과.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

}
