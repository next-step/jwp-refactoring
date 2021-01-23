package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 서비스")
public class MenuGroupServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        MenuGroupResponse savedMenuGroup = menuGroupService.create(createRequest("추천메뉴"));

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("모든 메뉴 그룹을 조회한다")
    @Test
    void findAll() {
        menuGroupService.create(createRequest("추천메뉴"));
        menuGroupService.create(createRequest("점심특선"));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
        List<String> menuGroupNames = menuGroups.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains("추천메뉴", "점심특선");
    }

    public static MenuGroupRequest createRequest(String name) {
        return new MenuGroupRequest(name);
    }
}
