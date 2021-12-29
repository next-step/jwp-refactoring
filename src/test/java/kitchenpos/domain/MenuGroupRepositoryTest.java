package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 레파지토리 테스트")
@DataJpaTest
class MenuGroupRepositoryTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 저장한다.")
    @Test
    void saveMenuGroup() {
        final MenuGroup menuGroup = MenuGroup.from("한식그룹");
        final MenuGroup actual = menuGroupRepository.save(menuGroup);

        assertThat(actual).isEqualTo(menuGroup);
    }

    @DisplayName("등록된 메뉴 그룹을 조회한다.")
    @Test
    void findMenuGroup() {
        final MenuGroup menuGroup = MenuGroup.from("한식그룹");
        final MenuGroup persistMenuGroup = menuGroupRepository.save(menuGroup);

        final MenuGroup actual = menuGroupRepository.findById(persistMenuGroup.getId()).get();

        assertThat(actual).isEqualTo(persistMenuGroup);
    }
}
