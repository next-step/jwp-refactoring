package kitchenpos.table;

import static kitchenpos.table.application.TableServiceTest.두명;
import static kitchenpos.table.application.TableServiceTest.비어있음;
import static kitchenpos.table.application.TableServiceTest.비어있지않음;
import static kitchenpos.table.ui.TableRestControllerTest.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.type.IntegerType.ZERO;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문테이블 관련 기능")
public class TableAcceptanceTest extends AcceptancePerMethodTest {

    @DisplayName("주문테이블 관리")
    @Test
    void manage() {
        // When
        ExtractableResponse<Response> 등록_응답 = 주문테이블_등록_요청(두명, 비어있음);
        // Then
        주문테이블이_등록됨(등록_응답);
        Long 주문테이블_ID = 주문테이블_등록_응답에서_주문테이블ID_가져오기(등록_응답);

        // When
        ExtractableResponse<Response> 목록_조회_응답 = 주문테이블_목록_조회_요청();
        // Then
        주문테이블_목록_조회됨(목록_조회_응답, 1);

        // When
        ExtractableResponse<Response> 주문테이블_비움상태_변경_응답 = 주문테이블_비움상태_변경_요청(주문테이블_ID, 비어있지않음);
        // Then
        주문테이블이_비어있지_않음(주문테이블_비움상태_변경_응답);

        // When
        ExtractableResponse<Response> 주문테이블_손님수_초기화_응답 = 주문테이블_손님수_변경_요청(주문테이블_ID, ZERO);
        // Then
        주문테이블_손님수_변경됨(주문테이블_손님수_초기화_응답);

        // When
        ExtractableResponse<Response> 주문테이블_비움_응답 = 주문테이블_비움상태_변경_요청(주문테이블_ID, 비어있음);
        // Then
        주문테이블이_비어있음(주문테이블_비움_응답);
    }

    private void 주문테이블이_비어있음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문테이블_손님수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문테이블_손님수_변경_요청(Long id, int numberOfGuests) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        return put(params, BASE_URL + "/" + id + "/number-of-guests");
    }

    private void 주문테이블이_비어있지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문테이블_비움상태_변경_요청(Long id, Boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("empty", empty);
        return put(params, BASE_URL + "/" + id + "/empty");
    }

    public static ExtractableResponse<Response> 주문테이블_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static List<OrderTableResponse> 주문테이블_목록_조회됨(ExtractableResponse<Response> response, Integer 주문테이블수) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderTableResponse> 목록_조회_응답 = new ArrayList<>(response.jsonPath().getList(".", OrderTableResponse.class));
        assertThat(목록_조회_응답).hasSize(주문테이블수);
        return 목록_조회_응답;
    }

    public static ExtractableResponse<Response> 주문테이블_등록_요청(Integer numberOfGuests, boolean empty) {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests);
        params.put("empty", empty);

        return post(params, BASE_URL);
    }

    public static void 주문테이블이_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static Long 주문테이블_등록_응답에서_주문테이블ID_가져오기(ExtractableResponse<Response> 등록_응답) {
        return Long.parseLong(등록_응답.header("Location").split(BASE_URL + "/")[1]);
    }

}
