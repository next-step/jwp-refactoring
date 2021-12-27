package kitchenpos.acceptance.step;

import static kitchenpos.testutils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductAcceptStep {

	private static final String BASE_URL = "/api/products";

	public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest.Create product) {
		return post(BASE_URL, product);
	}

	public static ProductResponse 상품_등록_확인(ExtractableResponse<Response> 등록_응답, ProductRequest.Create 등록_요청_상품) {
		ProductResponse 등록된_상품 = 등록_응답.as(ProductResponse.class);
		assertAll(
			() -> assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(등록된_상품).satisfies(등록된_상품_확인(등록_요청_상품))
		);
		return 등록된_상품;
	}

	private static Consumer<ProductResponse> 등록된_상품_확인(ProductRequest.Create 등록_요청_데이터) {
		return product -> {
			assertThat(product.getId()).isNotNull();
			assertThat(product.getName()).isEqualTo(등록_요청_데이터.getName());
			assertThat(product.getPrice()).isEqualByComparingTo(등록_요청_데이터.getPrice());
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

	public static ProductResponse 상품이_등록되어_있음(String name, int price) {
		ProductRequest.Create 상품_등록_요청_데이터 = new ProductRequest
			.Create(name, BigDecimal.valueOf(price));

		return 상품_등록_요청(상품_등록_요청_데이터).as(ProductResponse.class);
	}
}
