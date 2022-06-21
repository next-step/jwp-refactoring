package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcTemplateMenuGroupDaoTest {
    private final JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    public JdbcTemplateMenuGroupDaoTest(JdbcTemplateMenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Test
    void save() {
        // given
        MenuGroup request = new MenuGroup("두마리통닭메뉴");

        // when
        MenuGroup saved = menuGroupDao.save(request);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findById() {
        // when
        MenuGroup menuGroup = menuGroupDao.findById(1L).get();

        // then
        assertThat(menuGroup.getId()).isNotNull();
    }

    @Test
    void findAll() {
        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    void existsById() {
        // when
        boolean exists = menuGroupDao.existsById(1L);

        // then
        assertThat(exists).isTrue();
    }
}
