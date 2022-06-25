package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@DisplayName("메뉴 그룹 관련")
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    MenuGroupService menuGroupService;
    @MockBean
    MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void create() {
        // given
        MenuGroup given = new MenuGroup("중식");

        // when
        menuGroupService.create(given);

        // then
        verify(menuGroupDao).save(given);
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupDao).findAll();
    }
}
