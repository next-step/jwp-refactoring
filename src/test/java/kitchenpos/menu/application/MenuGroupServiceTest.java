package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.fixture.MenuGroupFixture;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 두마리_메뉴그룹;
    private MenuGroup 한마리_메뉴그룹;

    @BeforeEach
    void setUp() {
        두마리_메뉴그룹 = MenuGroupFixture.create(1L, "두마리메뉴");
        한마리_메뉴그룹 = MenuGroupFixture.create(2L, "한마리메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupRepository.save(두마리_메뉴그룹)).willReturn(두마리_메뉴그룹);

        MenuGroup savedMenuGroup = menuGroupService.create(두마리_메뉴그룹);

        assertThat(savedMenuGroup).isEqualTo(두마리_메뉴그룹);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(두마리_메뉴그룹, 한마리_메뉴그룹));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).containsExactly(두마리_메뉴그룹, 한마리_메뉴그룹);
    }
}