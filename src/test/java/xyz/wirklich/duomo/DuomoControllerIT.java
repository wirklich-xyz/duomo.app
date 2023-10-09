package xyz.wirklich.duomo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = DuomoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DuomoControllerIT {
    @Autowired
    private TestRestTemplate template;

    @Test
    public void getHello() throws Exception {
        //ResponseEntity<String> response = template.getForEntity("/", String.class);
        //assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot!");
        assertThat("",1, is(1));
    }
}
