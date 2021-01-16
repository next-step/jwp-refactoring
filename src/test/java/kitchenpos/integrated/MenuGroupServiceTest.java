package kitchenpos.integrated;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 기능 테스트")
@SpringBootTest
class MenuGroupServiceTest {
    private MenuGroup menuGroup1;

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup1 = new MenuGroup("후라이드치킨");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() throws Exception {
        // when
        MenuGroupResponse createdMenuGroup = menuGroupService.create(new MenuGroupRequest(this.menuGroup1.getName()));

        // then
        assertThat(createdMenuGroup).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(this.menuGroup1.getName());
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.findAll();

        // then
        assertThat(menuGroupResponses).isNotEmpty();
    }
}
