package kitchenpos.order.table.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kitchenpos.order.table.client.dto.TableDto;

@FeignClient(name = "table", url = "${kitchenpos.table.url}")
public interface TableClient {

	@PostMapping("/api/tables/ungroup/{orderTableId}")
	void ungroup(@PathVariable long orderTableId);

	@PostMapping("/api/tables/{orderTableId}/empty")
	void changeEmpty(@PathVariable("orderTableId") long orderTableId);

	@GetMapping("/api/table/{orderTableId}")
	TableDto getTable(@PathVariable long orderTableId);

}
