package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup saveMenuGroup;

    @BeforeEach
    public void setup() {
        saveMenuGroup = menuGroupRepository.save(new MenuGroup("새로운 메뉴"));
    }

    @Test
    @DisplayName("생성한 메뉴를을 저장 한다")
    @Transactional
    public void createMenu(){
        //given
        String name = "후라이드치킨";
        BigDecimal price = new BigDecimal(16000);
        Menu menu = new Menu(name, price, saveMenuGroup);

        // when
        Menu saveMenu = menuRepository.save(menu);

        // then
        assertThat(saveMenu).isEqualTo(menu);
    }

    @Test
    @DisplayName("메뉴 리스트를 가져온다")
    public void selectMenuList(){
        // when
        List<Menu> menus = menuRepository.findAll();

        // then
        assertThat(menus).isNotEmpty();
        for (Menu menu : menus) {
            assertThat(menu.id()).isNotNull();
        }
    }
}
