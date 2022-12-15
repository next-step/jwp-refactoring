package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroupServiceTest")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹 생성")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void create(String name) {
        assertThat(menuGroupService.create(new MenuGroupCreateRequest(name)).getName())
                .isEqualTo(name);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void name() {
        menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹 A")).getName();
        assertThat(menuGroupService.list()).hasSize(1);
    }
}
