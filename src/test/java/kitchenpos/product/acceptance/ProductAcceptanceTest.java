package kitchenpos.product.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.product.rest.ProductRestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("신규 상품 정보가 주어진 경우 상품 등록 요청시 요청에 성공한다")
    void createProductThenReturnProductInfoResponse() {
        // given
        ProductCreateRequest request = ProductCreateRequest.of("강정치킨", 12_000L);

        // when
        ExtractableResponse<Response> response = ProductRestAssured.상품_등록_요청(request);

        // then
        ProductResponse productResponse = response.as(ProductResponse.class);
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(productResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("상품 목록 조회 요청시 요청에 성공한다")
    void findAllProductsThenReturnProductInfoResponses() {
        // given
        ProductRestAssured.상품_등록됨(ProductFixture.강정치킨);

        // when
        ExtractableResponse<Response> response = ProductRestAssured.상품_목록_조회_요청();

        // then
        List<ProductResponse> productResponses = response.as(new TypeRef<List<ProductResponse>>() {});
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(productResponses).hasSize(1)
        );
    }
}
