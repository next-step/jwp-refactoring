package kitchenpos.ui.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kitchenpos.AcceptanceTest;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("메뉴 그룹 관련 기능 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("메뉴 그룹 관련 기능 정상 시나리오")
    void successTest() {
        //when
        ResponseEntity<MenuGroupResponse> 메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청("추천메뉴");
        //then
        메뉴_그룹_등록됨(메뉴_그룹_등록_응답_결과);

        //when
        ResponseEntity<List<MenuGroupResponse>> 메뉴_그룹_조회_응답_결과 = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_목록_조회됨(메뉴_그룹_조회_응답_결과, "추천메뉴");
    }

    @TestFactory
    @DisplayName("메뉴 그룹 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("메뉴 그룹명 없이 메뉴 그룹 등록 요청하면 메뉴 그룹 등록 실패한다.", () -> {
                    ResponseEntity<MenuGroupResponse> 메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청(null);

                    메뉴_그룹_등록_실패됨(메뉴_그룹_등록_응답_결과);
                }),
                dynamicTest("매우 긴 이름으로 메뉴 그룹 등록 요청하면 메뉴 그룹 등록 실패한다.", () -> {
                    String 매우_긴_이름 = 매우_긴_이름_생성();

                    ResponseEntity<MenuGroupResponse> 메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청(매우_긴_이름);

                    메뉴_그룹_등록_실패됨(메뉴_그룹_등록_응답_결과);
                })
        );
    }

    public static MenuGroupResponse 메뉴_그룹_등록되어_있음(String name) {
        return 메뉴_그룹_등록_요청(name).getBody();
    }

    public static ResponseEntity<MenuGroupResponse> 메뉴_그룹_등록_요청(String name) {
        MenuGroupRequest menuGroup = new MenuGroupRequest(name);
        return testRestTemplate.postForEntity("/api/menu-groups", menuGroup, MenuGroupResponse.class);
    }

    private void 메뉴_그룹_목록_조회됨(ResponseEntity<List<MenuGroupResponse>> response, String... names) {
        List<String> menuGroupNames = 메뉴_그룹_이름_목록_조회(response);
        assertThat(menuGroupNames).containsExactly(names);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    private List<String> 메뉴_그룹_이름_목록_조회(ResponseEntity<List<MenuGroupResponse>> response) {
        return response.getBody().stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<MenuGroupResponse>> 메뉴_그룹_조회_요청() {
        return testRestTemplate.exchange("/api/menu-groups", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuGroupResponse>>() {});
    }

    private void 메뉴_그룹_등록됨(ResponseEntity<MenuGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private void 메뉴_그룹_등록_실패됨(ResponseEntity<MenuGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String 매우_긴_이름_생성() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            value.append("메뉴");
        }
        return value.toString();
    }
}
