package kitchenpos.product.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptStep {
    private static final String BASE_URL = "/api/products";

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static ProductResponse 상품이_등록되어_있음(String name, BigDecimal price) {
        ProductRequest 등록_요청_데이터 = ProductRequest.of(name, price);

        return 상품_등록_요청(등록_요청_데이터).as(ProductResponse.class);
    }

    public static ProductResponse 상품_등록_확인(ExtractableResponse<Response> 상품_등록_응답, ProductRequest 등록_요청_데이터) {
        ProductResponse 등록된_상품 = 상품_등록_응답.as(ProductResponse.class);

        assertAll(
                () -> assertThat(상품_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_상품).satisfies(등록된_상품_확인(등록_요청_데이터))
        );

        return 등록된_상품;
    }

    private static Consumer<ProductResponse> 등록된_상품_확인(ProductRequest 등록_요청_데이터) {
        return productResponse -> {
            assertThat(productResponse.getId()).isNotNull();
            assertThat(productResponse.getName()).isEqualTo(등록_요청_데이터.getName());
            assertThat(productResponse.getPrice()).isEqualByComparingTo(등록_요청_데이터.getPrice());
        };
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 상품_목록_조회_확인(ExtractableResponse<Response> 상품_목록_조회_응답, ProductResponse 등록된_상품) {
        List<ProductResponse> 조회된_상품_목록 = 상품_목록_조회_응답.as(new TypeRef<List<ProductResponse>>() {
        });

        assertAll(
                () -> assertThat(상품_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_상품_목록).satisfies(조회된_상품_목록_확인(등록된_상품))
        );
    }

    private static Consumer<List<? extends ProductResponse>> 조회된_상품_목록_확인(ProductResponse 등록된_상품) {
        return productResponses -> {
            assertThat(productResponses.size()).isOne();
            assertThat(productResponses).first()
                    .satisfies(productResponse -> {
                        assertThat(productResponse.getId()).isEqualTo(등록된_상품.getId());
                        assertThat(productResponse.getName()).isEqualTo(등록된_상품.getName());
                        assertThat(productResponse.getPrice()).isEqualByComparingTo(등록된_상품.getPrice());
                    });
        };
    }
}
