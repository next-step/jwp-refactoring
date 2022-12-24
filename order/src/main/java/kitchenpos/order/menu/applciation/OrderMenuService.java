package kitchenpos.order.menu.applciation;

import org.springframework.stereotype.Service;

import kitchenpos.common.domain.Price;
import kitchenpos.order.menu.client.MenuClient;
import kitchenpos.order.menu.client.dto.MenuDto;
import kitchenpos.order.menu.domain.Menu;

@Service
public class OrderMenuService {

	private final MenuClient menuClient;

	public OrderMenuService(MenuClient menuClient) {
		this.menuClient = menuClient;
	}

	public Menu menu(Long id) {
		MenuDto dto = menuClient.menu(id);
		return Menu.of(dto.getId(), dto.getName(), Price.from(dto.getPrice()));
	}
}
