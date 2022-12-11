package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "피자");
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(result.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "피자");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroup> results = menuGroupService.list();

        // then
        assertThat(results).hasSize(1)
            .containsExactly(menuGroup);
    }
}
