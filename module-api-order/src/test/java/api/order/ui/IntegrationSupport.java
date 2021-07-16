package api.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class IntegrationSupport {
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void tearDown() {
        this.entityManager
                .createNativeQuery("ALTER TABLE order_table ALTER COLUMN `id` RESTART WITH 9")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu_product ALTER COLUMN `seq` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu ALTER COLUMN `id` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE product ALTER COLUMN `id` RESTART WITH 7")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE menu_group ALTER COLUMN `id` RESTART WITH 5")
                .executeUpdate();
    }

    protected MockHttpServletRequestBuilder postAsJson(String url, Object object) {
        try {
            return post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(object))
                    .accept(MediaType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected MockHttpServletRequestBuilder putAsJson(String url, Object object) {
        try {
            return put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(object))
                    .accept(MediaType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T toObject(MvcResult mvcResult, Class<T> tClass) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), tClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> List<T> toList(MvcResult mvcResult, Class<T> tClass) {
        try {
            return objectMapper.readValue(
                    mvcResult.getResponse().getContentAsString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
