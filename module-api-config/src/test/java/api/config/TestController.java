package api.config;

import common.application.NotFoundException;
import common.application.ValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/not-found")
	public void badRequest() {
		throw new NotFoundException("for test");
	}

	@GetMapping("/validation")
	public void validation() {
		throw new ValidationException("for test");
	}

	@GetMapping("/internal")
	public void internal() {
		throw new RuntimeException("for test");
	}

}
