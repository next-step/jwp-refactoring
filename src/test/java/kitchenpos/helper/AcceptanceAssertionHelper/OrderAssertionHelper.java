package kitchenpos.helper.AcceptanceAssertionHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.http.HttpStatus;

public class OrderAssertionHelper {

    public static void 오더_등록되어있음(ExtractableResponse<Response> 등록결과) {
        assertAll(
            () -> assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(등록결과.jsonPath().get("id").toString()).isNotNull()
        );
    }

    public static void 오더_리스트_조회됨(ExtractableResponse<Response> 조회결과, List<OrderResponse> 등록_오더_리스트) {
        assertAll(
            () -> assertThat(조회결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(조회결과.jsonPath().getList("."))
                .hasSize(등록_오더_리스트.size())
                .extracting("id").isEqualTo(
                    등록_오더_리스트.stream()
                        .map(테이블 -> 테이블.getId().intValue())
                        .collect(Collectors.toList())
                )
        );
    }

    public static void 오더_상태_설정됨(ExtractableResponse<Response> 설정결과, String 오더상태) {
        assertAll(
            () -> assertThat(설정결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(설정결과.jsonPath().get("orderStatus").toString()).isEqualTo(오더상태)
        );
    }

    public static void 오더_설정_에러(ExtractableResponse<Response> 설정결과) {
        assertThat(설정결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
