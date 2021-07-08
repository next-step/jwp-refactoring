package kitchenpos.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public abstract class MockMvcControllerTest {
    protected MockMvc mockMvc;

    abstract protected Object controller();

    @BeforeEach
    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller())
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    protected <T> ArrayList<T> toArrayListBy(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }
}
