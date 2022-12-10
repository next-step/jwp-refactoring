package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of(1L, "menuGroup");
    }

    @Test
    @DisplayName("메뉴 그룹 생성")
    public void createMenuGroup() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 조회")
    public void queryMenuGroup() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(1);
    }
}