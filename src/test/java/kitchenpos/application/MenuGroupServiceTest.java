package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 고기_메뉴그룹;
    private MenuGroup 야채_메뉴그룹;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        야채_메뉴그룹 = MenuGroupFixtureFactory.create(2L, "야채 메뉴그룹");
    }

    @DisplayName("MenuGroup 을 등록한다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = MenuGroup.from("고기 메뉴그룹");
        given(menuGroupRepository.save(menuGroup)).willReturn(고기_메뉴그룹);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).isEqualTo(고기_메뉴그룹);
    }

    @DisplayName("MenuGroup 목록을 조회한다.")
    @Test
    void findList() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(고기_메뉴그룹, 야채_메뉴그룹));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(고기_메뉴그룹, 야채_메뉴그룹);
    }
}