package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 인수 테스트")
class MenuGroupAcceptanceTest extends MenuGroupAcceptanceTestFixture {

    /**
     *   When 메뉴 그룹을 생성하면
     *   Then 메뉴 그룹이 생성된다
     */
    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void 메뉴_그룹_생성() {
        // When
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(세트);

        // Then
        메뉴_그룹_생성됨(response);

        // Then
        MenuGroupResponse 생성된_메뉴_그룹 = 메뉴_그룹(response);
        assertAll(
                () -> assertThat(생성된_메뉴_그룹.getId()).isNotNull(),
                () -> assertThat(생성된_메뉴_그룹.getName()).isEqualTo("세트")
        );
    }

    /**
     *   Given 메뉴 그룹을 등록하고
     *   When 메뉴 그룹 목록을 조회화면
     *   Then 메뉴 그룹 목록이 조회된다
     */
    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void 메뉴_그룹_목록_조회() {
        // Given
        MenuGroupRequest 단품 = new MenuGroupRequest("단품");
        MenuGroupResponse 생성된_메뉴_그룹_1 = 메뉴_그룹(메뉴_그룹_생성_요청(세트));
        MenuGroupResponse 생성된_메뉴_그룹_2 = 메뉴_그룹(메뉴_그룹_생성_요청(단품));

        // When
        ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();

        // Then
        메뉴_그룹_목록_조회됨(response);

        // Then
        List<MenuGroupResponse> 조회된_메뉴_그룹_목록 = 메뉴_그룹_목록(response);
        assertThat(조회된_메뉴_그룹_목록).containsAll(Arrays.asList(생성된_메뉴_그룹_1, 생성된_메뉴_그룹_2));
    }

}