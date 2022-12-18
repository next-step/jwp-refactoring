package kitchenpos.menugroup.application;

import static kitchenpos.menugroup.domain.MenuGroupFixture.인기메뉴;
import static kitchenpos.menugroup.domain.MenuGroupFixture.추천메뉴;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup given = 추천메뉴;
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(given);

        MenuGroupResponse actual = menuGroupService.create(new MenuGroupRequest(given.getName()));

        assertThat(actual.getName()).isEqualTo(given.getName());
    }

    @DisplayName("메뉴 그룹명이 비어있거나 공백인 메뉴 그룹은 등록할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNameIsNullOrEmpty(String name) {
        MenuGroupRequest given = new MenuGroupRequest(name);

        assertThatThrownBy(() -> menuGroupService.create(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹명은 비어있거나 공백일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<MenuGroup> expectedMenuGroups = Arrays.asList(추천메뉴, 인기메뉴);
        given(menuGroupRepository.findAll()).willReturn(expectedMenuGroups);

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(2);
        assertThat(actual.stream().map(MenuGroupResponse::getName)).contains(추천메뉴.getName(), 인기메뉴.getName());
    }
}
