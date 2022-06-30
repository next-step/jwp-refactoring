package kitchenpos.helper.AcceptanceAssertionHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.helper.testDTO.response.ProductResponse;
import org.springframework.http.HttpStatus;

public class ProductAssertionHelper {

    public static void 상품_등록되어있음(ExtractableResponse<Response> 등록결과, String 상품명) {
        assertAll(
            () -> assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(등록결과.jsonPath().get("id").toString()).isNotNull(),
            () -> assertThat(등록결과.jsonPath().get("name").toString()).isEqualTo(상품명)
        );
    }

    public static void 상품_리스트_조회됨(ExtractableResponse<Response> 조회결과,
        List<ProductResponse> 등록상품_리스트) {
        assertAll(
            () -> assertThat(조회결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(조회결과.jsonPath().getList("."))
                .hasSize(등록상품_리스트.size())
                .extracting("name").isEqualTo(
                    등록상품_리스트.stream()
                        .map(ProductResponse::getName)
                        .collect(Collectors.toList())
                )
        );
    }

    public static void 상품_등록_에러발생(ExtractableResponse<Response> 등록결과) {
        assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
