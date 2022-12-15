package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends ProductAcceptanceTestFixture {

    /**
     *   When 상품을 생성하면
     *   Then 상품이 생성된다
     */
    @DisplayName("상품을 생성한다")
    @Test
    void 상품_생성() {
        // When
        ExtractableResponse<Response> response = 상품_생성_요청(떡볶이);

        // Then
        상품_생성됨(response);

        // Then
        Product 생성된_상품 = 상품(response);
        assertAll(
                () -> assertThat(생성된_상품.getId()).isNotNull(),
                () -> assertThat(생성된_상품.getName()).isEqualTo("떡볶이")
        );
    }

    /**
     *   Given 상품을 등록하고
     *   When 상품 목록을 조회화면
     *   Then 상품 목록이 조회된다
     */
    @DisplayName("상품 목록을 조회한다")
    @Test
    void 상품_목록_조회() {
        // Given
        Product 생성된_상품_1 = 상품_생성_되어있음(떡볶이);
        Product 생성된_상품_2 = 상품_생성_되어있음(튀김);
        Product 생성된_상품_3 = 상품_생성_되어있음(순대);
        // When
        ExtractableResponse<Response> response = 상품_조회_요청();

        // Then
        상품_목록_조회됨(response);

        // Then
        List<Product> 조회된_상품_목록 = 상품_목록(response);
        assertThat(조회된_상품_목록).containsAll(Arrays.asList(생성된_상품_1, 생성된_상품_2, 생성된_상품_3));
    }
}
