package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuRepository 테스트")
@DataJpaTest
class MenuRepositoryTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private MenuGroup 양식;

    @BeforeEach
    void setUp() {
        양식 = menuGroupRepository.save(new MenuGroup("양식"));
    }

    @Test
    void 메뉴_Id_목록을_입력받아_메뉴_목록_조회() {
        Menu 양식_세트1 = menuRepository.save(new Menu("양식 세트1", new BigDecimal(50000), 양식));
        Menu 양식_세트2 = menuRepository.save(new Menu("양식 세트2", new BigDecimal(43000), 양식));

        List<Menu> menus = menuRepository.findAllById(Arrays.asList(양식_세트1.getId(), 양식_세트2.getId()));

        assertThat(menus).hasSize(2);
    }
}
