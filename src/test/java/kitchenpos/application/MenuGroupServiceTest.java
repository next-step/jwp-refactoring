package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    public void create() {
        MenuGroup mockMenuGroup = mock(MenuGroup.class);
        given(menuGroupDao.save(any()))
                .willReturn(mockMenuGroup);
        MenuGroup result = menuGroupService.create(new MenuGroup());
        assertThat(result).isEqualTo(mockMenuGroup);
        verify(menuGroupDao, atMostOnce())
                .save(any());
    }

    @DisplayName("조회 테스트")
    @Test
    public void list() {
        List<MenuGroup> mockList = mock(List.class);
        given(menuGroupDao.findAll())
                .willReturn(mockList);
        assertThat(menuGroupService.list()).isEqualTo(mockList);
    }


}
