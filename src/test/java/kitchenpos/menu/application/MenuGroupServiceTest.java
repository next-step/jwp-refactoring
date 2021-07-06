package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹이 정상적으로 등록된다.")
    @Test
    void create() {
        // Given
        MenuGroup 한식 = new MenuGroup(1L, "한식");
        given(menuGroupDao.save(any())).willReturn(한식);

        // When
        menuGroupService.create(MenuGroupRequest.of(한식));

        // Then
        verify(menuGroupDao, times(1)).save(any());
    }

    @DisplayName("메뉴 그룹 목록이 정상적으로 조회된다.")
    @Test
    void list() {
        // Given
        List<MenuGroup> menuGroups = new ArrayList<>();
        menuGroups.add(new MenuGroup(1L, "한식"));
        menuGroups.add(new MenuGroup(2L, "중식"));
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // When & Then
        assertThat(menuGroupService.list()).hasSize(2);
        verify(menuGroupDao, times(1)).findAll();
    }
}
