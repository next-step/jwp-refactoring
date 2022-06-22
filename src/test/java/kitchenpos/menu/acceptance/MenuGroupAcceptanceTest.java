package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("메뉴 그룹 관련 기능 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * Feature 메뉴 그룹 관련 기능
     *
     * Scenario 메뉴 그룹 관련 기능
     * Given 잘못된 이름
     * When 메뉴 그룹 등록 요청
     * Then 메뉴 그룹 등록 실패됨
     *
     * When 메뉴 그룹 등록 요청
     * Then 메뉴 그룹 등록됨
     * When 메뉴 그룹 목록 조회 요청
     * Then 메뉴 그룹 목록 조회됨
     */
    @Test
    @DisplayName("메뉴 그룹 관련 기능")
    void integrationTest() {
        //given
        String 잘못된_이름 = null;
        //when
        ResponseEntity<MenuGroup> 잘못된_이름_메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청(잘못된_이름);
        //then
        메뉴_그룹_등록_실패됨(잘못된_이름_메뉴_그룹_등록_응답_결과);

        //when
        ResponseEntity<MenuGroup> 메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청("추천메뉴");
        //then
        메뉴_그룹_등록됨(메뉴_그룹_등록_응답_결과);

        //when
        ResponseEntity<List<MenuGroup>> 메뉴_그룹_조회_응답_결과 = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_목록_조회됨(메뉴_그룹_조회_응답_결과, "추천메뉴");
    }

    public static ResponseEntity<MenuGroup> 메뉴_그룹_등록_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return testRestTemplate.postForEntity("/api/menu-groups", menuGroup, MenuGroup.class);
    }

    private void 메뉴_그룹_목록_조회됨(ResponseEntity<List<MenuGroup>> response, String... names) {
        List<String> menuGroupNames = 메뉴_그룹_이름_목록_조회(response);
        assertThat(menuGroupNames).containsExactly(names);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    private List<String> 메뉴_그룹_이름_목록_조회(ResponseEntity<List<MenuGroup>> response) {
        return response.getBody().stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<MenuGroup>> 메뉴_그룹_조회_요청() {
        return testRestTemplate.exchange("/api/menu-groups", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuGroup>>() {});
    }

    private void 메뉴_그룹_등록됨(ResponseEntity<MenuGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private void 메뉴_그룹_등록_실패됨(ResponseEntity<MenuGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
