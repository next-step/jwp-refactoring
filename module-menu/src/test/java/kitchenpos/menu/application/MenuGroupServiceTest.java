package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.fixture.MenuGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 분식_메뉴그룹;
    private MenuGroup 초밥_메뉴그룹;

    @BeforeEach
    void setUp() {
        분식_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "분식 메뉴그룹");
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(2L, "초밥 메뉴그룹");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create01() {
        // given
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from("초밥_메뉴그룹");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(초밥_메뉴그룹);

        // when
        MenuGroupResponse actualMenuGroupResponse = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(actualMenuGroupResponse).isEqualTo(MenuGroupResponse.from(초밥_메뉴그룹));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Lists.newArrayList(분식_메뉴그룹, 초밥_메뉴그룹));

        // when
        List<MenuGroupResponse> actualMenuGroups = menuGroupService.list();

        // then
        assertThat(actualMenuGroups).containsExactly(
                MenuGroupResponse.from(분식_메뉴그룹),
                MenuGroupResponse.from(초밥_메뉴그룹)
        );
    }
}