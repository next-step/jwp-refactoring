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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천메뉴");
    }

    @Test
    @DisplayName("메뉴그룹 등록")
    void create() {
        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        assertThat(menuGroupService.create(menuGroup)).isNotNull();
    }

    @Test
    @DisplayName("모든 메뉴그룹 조회")
    void list() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup));

        assertThat(menuGroupService.list()).isNotNull();
        assertThat(menuGroupService.list().size()).isEqualTo(1);
        assertThat(menuGroupService.list().get(0).getName()).isEqualTo("추천메뉴");
    }
}