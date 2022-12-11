package kitchenpos.menugroup;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> menuGroup() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 등록한다.", () -> {
                    ResponseEntity<MenuGroup> response = 메뉴_그룹_생성_요청("신메뉴");

                    메뉴_그룹_생성됨(response);
                }),
                dynamicTest("이름이 없는 메뉴 그룹을 등록한다.", () -> {
                    ResponseEntity<MenuGroup> response = 메뉴_그룹_생성_요청(null);

                    메뉴_그룹_생성_실패됨(response);
                }),
                dynamicTest("메뉴 그룹 목록을 조회한다.", () -> {
                    ResponseEntity<List<MenuGroup>> response = 메뉴_그룹_목록_조회_요청();

                    메뉴_그룹_목록_응답됨(response);
                    메뉴_그룹_목록_확인됨(response, "신메뉴");
                })
        );
    }

    public static MenuGroup 메뉴_그룹_등록됨(String name) {
        return 메뉴_그룹_생성_요청(name).getBody();
    }

    public static ResponseEntity<MenuGroup> 메뉴_그룹_생성_요청(String name) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        return restTemplate().postForEntity("/api/menu-groups", request, MenuGroup.class);
    }

    public static ResponseEntity<List<MenuGroup>> 메뉴_그룹_목록_조회_요청() {
        return restTemplate().exchange("/api/menu-groups", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<MenuGroup>>() {});
    }

    public static void 메뉴_그룹_생성됨(ResponseEntity<MenuGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    public static void 메뉴_그룹_생성_실패됨(ResponseEntity<MenuGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 메뉴_그룹_목록_응답됨(ResponseEntity<List<MenuGroup>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 메뉴_그룹_목록_확인됨(ResponseEntity<List<MenuGroup>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(MenuGroup::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
