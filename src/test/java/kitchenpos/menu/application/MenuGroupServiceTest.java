package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static kitchenpos.common.fixture.NameFixture.MENU_GROUP_A_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroupServiceTest")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        assertThat(menuGroupService.create(new MenuGroupCreateRequest(MENU_GROUP_A_NAME)).getName())
                .isEqualTo(MENU_GROUP_A_NAME);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void name() {
        menuGroupService.create(new MenuGroupCreateRequest(MENU_GROUP_A_NAME));
        assertThat(menuGroupService.list()).hasSize(1);
    }
}
