package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    private MenuGroup menuGroup2;

    private MenuGroupRequest menuGroupRequest;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of(1L, "추천메뉴");
        menuGroup2 = MenuGroup.of(2L, "한마리메뉴");
        menuGroupRequest = new MenuGroupRequest("두마리메뉴");
    }

    @DisplayName("메뉴그룹을 등록한다.")
    @Test
    void create() {
        // given
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        // when
        MenuGroup expected = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(menuGroup.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("메뉴그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<MenuGroup> actual = Arrays.asList(menuGroup, menuGroup2);
        when(menuGroupRepository.findAll()).thenReturn(actual);

        // when
        List<MenuGroup> expected = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
