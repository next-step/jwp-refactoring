package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static kitchenpos.menugroup.MenuGroupFixtures.메뉴그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : MenuGroupRepositoryTest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@DataJpaTest
@DisplayName("메뉴그룹 리파지토리 테스트")
class MenuGroupRepositoryTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    public void list() {
        //when
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        //then
        assertThat(menuGroups.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() {
        //when
        final MenuGroup actual = menuGroupRepository.save(메뉴그룹("한마리메뉴그룹"));

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("한마리메뉴그룹")
        );
    }
}
