package kitchenpos.helper.AcceptanceAssertionHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.http.HttpStatus;

public class TableAssertionHelper {

    public static void 테이블_등록되어있음(ExtractableResponse<Response> 등록결과, OrderTable 테이블_정보) {
        assertAll(
            () -> assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(등록결과.jsonPath().get("id").toString()).isNotNull()
        );
    }

    public static void 테이블_리스트_조회됨(ExtractableResponse<Response> 조회결과, List<OrderTable> 등록테이블_리스트) {
        assertAll(
            () -> assertThat(조회결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(조회결과.jsonPath().getList("."))
                .hasSize(등록테이블_리스트.size())
                .extracting("id").isEqualTo(
                    등록테이블_리스트.stream()
                        .map(테이블 -> 테이블.getId().intValue())
                        .collect(Collectors.toList())
                )
        );
    }

    public static void 테이블_유휴여부_설정됨(ExtractableResponse<Response> 설정결과, String 유휴여부){
        assertAll(
            () -> assertThat(설정결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(설정결과.jsonPath().get("empty").toString()).isEqualTo(유휴여부)
        );
    }

    public static void 테이블_손님수_설정됨(ExtractableResponse<Response> 설정결과, int 손님수){
        assertAll(
            () -> assertThat(설정결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(설정결과.jsonPath().get("numberOfGuests").toString()).isEqualTo(String.valueOf(손님수))
        );
    }

    public static void 테이블_손님수_설정_에러(ExtractableResponse<Response> 설정결과){
        assertThat(설정결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
