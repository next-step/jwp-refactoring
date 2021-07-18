package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("메뉴그룹 리파지토리 테스트")
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        // when
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리"));

        // then
        assertThat(menuGroup.getId()).isNotNull();
    }
}
