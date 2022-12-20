package kitchenpos;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.AcceptanceTest.restTemplate;
import static org.assertj.core.api.Assertions.assertThat;

public final class MenuGroupAcceptanceTestUtil {

    private MenuGroupAcceptanceTestUtil() {
    }

    public static MenuGroupResponse 메뉴_그룹_등록됨(String name) {
        return 메뉴_그룹_생성_요청(name).getBody();
    }

    static ResponseEntity<MenuGroupResponse> 메뉴_그룹_생성_요청(String name) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        return restTemplate().postForEntity("/api/menu-groups", request, MenuGroupResponse.class);
    }

    static ResponseEntity<List<MenuGroupResponse>> 메뉴_그룹_목록_조회_요청() {
        return restTemplate().exchange("/api/menu-groups", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<MenuGroupResponse>>() {});
    }

    static void 메뉴_그룹_생성됨(ResponseEntity<MenuGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    static void 메뉴_그룹_생성_실패됨(ResponseEntity<MenuGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 메뉴_그룹_목록_응답됨(ResponseEntity<List<MenuGroupResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 메뉴_그룹_목록_확인됨(ResponseEntity<List<MenuGroupResponse>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(MenuGroupResponse::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
