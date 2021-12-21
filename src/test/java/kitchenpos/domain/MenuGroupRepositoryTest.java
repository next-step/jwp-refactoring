package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.한마리메뉴그룹요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : kitchenpos.domain
 * fileName : MenuGroupRepositoryTest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@DataJpaTest
class MenuGroupRepositoryTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        //when
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        //then
        assertThat(menuGroups.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() throws Exception {
        //given
        MenuGroup 한마리메뉴 = 한마리메뉴그룹요청().toEntity();

        //when
        final MenuGroup actual = menuGroupRepository.save(한마리메뉴);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("한마리메뉴")
        );
    }
}
