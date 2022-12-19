package kitchenpos.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 커피류 = new MenuGroup("커피류");
        when(menuGroupRepository.save(커피류)).thenReturn(커피류);

        // when
        MenuGroup result = menuGroupRepository.save(커피류);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(커피류.getId()),
            () -> assertThat(result.getName()).isEqualTo(커피류.getName())
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup 커피류 = new MenuGroup("커피류");
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(커피류));

        // when
        List<MenuGroup> results = menuGroupRepository.findAll();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(커피류.getId()),
            () -> assertThat(results.get(0).getName()).isEqualTo(커피류.getName())
        );
    }
}
