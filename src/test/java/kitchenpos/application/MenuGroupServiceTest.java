package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.domain.MenuGroupTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 서비스")
class MenuGroupServiceTest {

    MenuGroupService menuGroupService;

    @Mock
    MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        when(menuGroupDao.save(menuGroup)).thenReturn(추천메뉴);

        // when
        MenuGroup savedGroup = menuGroupService.create(menuGroup);

        // then
        assertEquals(추천메뉴, savedGroup);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져온다")
    void list() {
        // given
        List<MenuGroup> groups = Stream.of(추천메뉴, 베스트메뉴, 세트메뉴)
            .collect(toList());
        when(menuGroupDao.findAll()).thenReturn(groups);

        // when
        List<MenuGroup> allGroups = menuGroupService.list();

        // then
        assertIterableEquals(groups, allGroups);
    }
}
