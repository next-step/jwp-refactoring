package kitchenpos.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
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

    public static ExtractableResponse<Response> 상품_등록_요청(Product 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static Product 상품이_등록되어_있음(String name, int price) {
        Product 등록_요청_데이터 = new Product();
        등록_요청_데이터.setName(name);
        등록_요청_데이터.setPrice(BigDecimal.valueOf(price));

        return 상품_등록_요청(등록_요청_데이터).as(Product.class);
    }

    public static Product 상품_등록_확인(ExtractableResponse<Response> 상품_등록_응답, Product 등록_요청_데이터) {
        Product 등록된_상품 = 상품_등록_응답.as(Product.class);

        assertAll(
                () -> assertThat(상품_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_상품).satisfies(등록된_상품_확인(등록_요청_데이터))
        );

        return 등록된_상품;
    }

    private static Consumer<Product> 등록된_상품_확인(Product 등록_요청_데이터) {
        return product -> {
            assertThat(product.getId()).isNotNull();
            assertThat(product.getName()).isEqualTo(등록_요청_데이터.getName());
            assertThat(product.getPrice()).isEqualByComparingTo(등록_요청_데이터.getPrice());
        };
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 상품_목록_조회_확인(ExtractableResponse<Response> 상품_목록_조회_응답, Product 등록된_상품) {
        List<Product> 조회된_상품_목록 = 상품_목록_조회_응답.as(new TypeRef<List<Product>>() {
        });

        assertAll(
                () -> assertThat(상품_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_상품_목록).satisfies(조회된_상품_목록_확인(등록된_상품))
        );
    }

    private static Consumer<List<? extends Product>> 조회된_상품_목록_확인(Product 등록된_상품) {
        return products -> {
            assertThat(products.size()).isOne();
            assertThat(products).first()
                    .satisfies(product -> {
                        assertThat(product.getId()).isEqualTo(등록된_상품.getId());
                        assertThat(product.getName()).isEqualTo(등록된_상품.getName());
                        assertThat(product.getPrice()).isEqualByComparingTo(등록된_상품.getPrice());
                    });
        };
    }
}
