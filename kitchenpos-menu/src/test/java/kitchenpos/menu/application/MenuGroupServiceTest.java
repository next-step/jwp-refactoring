package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuGroupTestFixture.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("면류");
        MenuGroup 면류 = request.toEntity();
        given(menuGroupRepository.save(면류)).willReturn(면류);

        // when
        MenuGroupResponse actual = menuGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("면류")
        );
    }

    @Test
    @DisplayName("메뉴 그룹을 조회하면 메뉴 그룹 목록을 반환한다.")
    void findMenuGroups() {
        MenuGroup 면류 = menuGroup(1L, "면류");
        MenuGroup 밥류 = menuGroup(2L, "밥류");
        List<MenuGroup> menuGroups = Arrays.asList(면류, 밥류);
        given(menuGroupRepository.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(MenuGroupResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly("면류", "밥류")
        );
    }
}
