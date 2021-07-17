package kitchenpos.menu.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final RabbitTemplate template;

    public MenuService(MenuRepository menuRepository, RabbitTemplate template) {
        this.menuRepository = menuRepository;
        this.template = template;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.toMenuProducts();
        Menu menu = new Menu(menuRequest.getName(), Price.wonOf(menuRequest.getPrice()), menuRequest.getMenuGroupId(), menuProducts);
        MenuResponse menuResponse = MenuResponse.of(menuRepository.save(menu));
        publishCreateMenuEvent(menuResponse);
        return menuResponse;
    }

    private void publishCreateMenuEvent(MenuResponse menuResponse) {
        try {
            String message = new ObjectMapper().writeValueAsString(menuResponse);
            template.convertAndSend("menuCreate-in-0.menuGroup", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("객체를 JSON 문자열로 변환하는 도중 예외가 발생했습니다.");
        }
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
