//package kitchenpos.domain.repository;
//
//import kitchenpos.domain.MenuGroup;
//import kitchenpos.dto.CreateMenuGroupRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class MenuGroupRepositoryTest {
//    public static final CreateMenuGroupRequest 햄버거_메뉴 = new CreateMenuGroupRequest("햄버거메뉴");
//
//    @Autowired
//    MenuGroupRepository menuGroupRepository;
//
//    @Test
//    @DisplayName("메뉴 그룹 생성")
//    void create() {
//        // when
//        final MenuGroup save = menuGroupRepository.save(햄버거_메뉴.toEntity());
//        // then
//        assertThat(save.getName()).isEqualTo("햄버거메뉴");
//    }
//}
