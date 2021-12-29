package kitchenpos.menugroup;

import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import java.util.List;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class MenuGroupAcceptanceFixtures {

    private static final String BASE_URL = "/api/menu-groups";

    public static ResponseEntity<List<MenuGroupResponse>> 메뉴그룹_전체_조회_요청() {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<MenuGroupResponse>>() {
            });
    }

    public static ResponseEntity<MenuGroupResponse> 메뉴그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, menuGroupRequest, MenuGroupResponse.class);
    }

    public static MenuGroupRequest 메뉴그룹_정의(String name) {
        return new MenuGroupRequest(name);
    }
}
