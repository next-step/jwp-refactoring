package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("name");
        MenuGroup expected = new MenuGroup(1L, "name");
        Mockito.when(menuGroupRepository.save(Mockito.any()))
            .thenReturn(expected);

        // when
        MenuGroup actual = menuGroupService.create(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = new MenuGroup(1L, "name1");
        MenuGroup menuGroup2 = new MenuGroup(2L, "name2");
        List<MenuGroup> expected = Arrays.asList(menuGroup1, menuGroup2);
        Mockito.when(menuGroupRepository.findAll())
            .thenReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}