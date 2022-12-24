package kitchenpos.order.menu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kitchenpos.order.menu.client.dto.MenuDto;

@FeignClient(name = "menu", url = "${kitchenpos.menu.url}", path = "/api/menus")
public interface MenuClient {

	@GetMapping
	MenuDto menu(@RequestParam(value = "id") Long id);
}
