package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
class MenuGroupServiceTest {

    @MockBean
    private MenuGroupDao menuGroupDao;

    MenuGroupService menuGroupService;
    MenuGroup 베스트, 남녀노소;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);

        베스트 = new MenuGroup();
        베스트.setId(1L);
        베스트.setName("베스트");

        남녀노소 = new MenuGroup();
        남녀노소.setId(2L);
        남녀노소.setName("남녀노소");
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        when(menuGroupDao.save(베스트)).thenReturn(베스트);
        assertThat(menuGroupService.create(베스트)).isEqualTo(베스트);
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(베스트, 남녀노소));
        assertThat(menuGroupService.list()).contains(베스트, 남녀노소);
    }
}