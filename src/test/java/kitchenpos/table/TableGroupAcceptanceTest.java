package kitchenpos.table;

import static kitchenpos.table.TableAcceptanceTest.주문테이블_등록_요청;
import static kitchenpos.table.TableAcceptanceTest.주문테이블_목록_조회_요청;
import static kitchenpos.table.TableAcceptanceTest.주문테이블_목록_조회됨;
import static kitchenpos.table.application.TableServiceTest.두명;
import static kitchenpos.table.application.TableServiceTest.비어있음;
import static kitchenpos.table.application.TableServiceTest.세명;
import static kitchenpos.table.ui.TableGroupRestControllerTest.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.AcceptancePerMethodTest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptancePerMethodTest {

    @DisplayName("단체지정 관리")
    @Test
    void manage() {
        주문테이블_등록_요청(두명, 비어있음);
        주문테이블_등록_요청(세명, 비어있음);

        ExtractableResponse<Response> 주문테이블_목록_조회_응답 = 주문테이블_목록_조회_요청();
        // Then
        List<OrderTableResponse> 주문테이블_목록 = 주문테이블_목록_조회됨(주문테이블_목록_조회_응답, 2);

        // When
        ExtractableResponse<Response> 단체지정_등록_응답 = 단체지정_등록_요청(주문테이블_목록);
        // Then
        단체지정이_등록됨(단체지정_등록_응답);
        Long 단체지정_ID = Long.parseLong(단체지정_등록_응답.header("Location").split(BASE_URL + "/")[1]);

        // When
        ExtractableResponse<Response> 단체지정_해제_응답 = 단체지정_해제_요청(단체지정_ID);
        // Then
        단체지정_해제됨(단체지정_해제_응답);
    }

    private ExtractableResponse<Response> 단체지정_해제_요청(Long tableGroupId) {
        return delete(BASE_URL + "/" + tableGroupId);
    }

    private void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 단체지정_등록_요청(List<OrderTableResponse> orderTableResponses) {
        List<Map<String, Long>> orderTables = new ArrayList<>();
        for (OrderTableResponse orderTableResponse : orderTableResponses) {
            Map<String, Long> params = new HashMap<>();
            params.put("id", orderTableResponse.getId());
            orderTables.add(params);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orderTables", orderTables);

        return post(params, BASE_URL);
    }

    public static void 단체지정이_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

}
