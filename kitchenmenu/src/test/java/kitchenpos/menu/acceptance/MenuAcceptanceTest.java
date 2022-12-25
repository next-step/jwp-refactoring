package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends MenuAcceptanceTestFixture {

    /**
     *   When 메뉴를 생성하면
     *   Then 메뉴가 생성된다
     */
    @DisplayName("메뉴를 생성한다")
    @Test
    void 메뉴_생성() {
        // When
        ExtractableResponse<Response> response = 메뉴_생성_요청(떡튀순);

        // Then
        메뉴_생성됨(response);

        // Then
        MenuResponse 생성된_메뉴 = 메뉴(response);
        assertAll(
                () -> assertThat(생성된_메뉴.getId()).isNotNull(),
                () -> assertThat(생성된_메뉴.getName()).isEqualTo("떡튀순")
        );
    }

    /**
     *   Given 메뉴를 등록하고
     *   When 메뉴 목록을 조회화면
     *   Then 메뉴 목록이 조회된다
     */
    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void 메뉴_목록_조회() {
        // Given
        MenuResponse 생성된_메뉴_1 = 메뉴_생성_되어있음(떡튀순);
        MenuResponse 생성된_메뉴_2 = 메뉴_생성_되어있음(떡튀순_곱배기);
        // When
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // Then
        메뉴_목록_조회됨(response);

        // Then
        List<MenuResponse> 조회된_메뉴_목록 = 메뉴_목록(response);
        assertThat(조회된_메뉴_목록).containsAll(Arrays.asList(생성된_메뉴_1, 생성된_메뉴_2));
    }
}
