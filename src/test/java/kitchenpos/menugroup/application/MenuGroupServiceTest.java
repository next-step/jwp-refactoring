package kitchenpos.menugroup.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    private final MenuGroup 분식 = new MenuGroup(1L,"분식");
    private final MenuGroup 한식 = new MenuGroup(2L,"한식");

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void createMenuGroupTest() {
        when(menuGroupDao.save(분식)).thenReturn(분식);

        assertThat(menuGroupService.create(분식)).isEqualTo(분식);
    }

    @Test
    void retrieveMenuGroupsTest() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(분식, 한식));

        assertThat(menuGroupService.list()).contains(분식, 한식);
    }
}
