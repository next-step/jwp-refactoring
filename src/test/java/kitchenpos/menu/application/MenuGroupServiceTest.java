package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.repository.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성("두마리메뉴");
        given(menuGroupRepository.save(menuGroup))
            .willReturn(menuGroup);

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertEquals(menuGroup.getName().getValue(), savedMenuGroup.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void getMenuGroups() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
            메뉴_그룹_생성("두마리메뉴"),
            메뉴_그룹_생성("한마리메뉴"));
        given(menuGroupRepository.findAll())
            .willReturn(menuGroups);

        // when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        // then
        assertThat(findMenuGroups)
            .extracting("name")
            .containsExactlyElementsOf(menuGroups.stream()
                .map(menuGroup -> menuGroup.getName().getValue())
                .collect(Collectors.toList())
            );
    }

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
