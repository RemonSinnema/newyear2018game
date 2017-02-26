package name.sinnema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Nye18Application {

  public static void main(String[] args) {
    SpringApplication.run(Nye18Application.class, args);
  }

  @RequestMapping(path = "/hello/{id}")
  public String hello(@PathVariable(name = "id") String id) {
    String name = id == null || id.trim().isEmpty() ? "world" : id;
    return "Hello, " + name;
  }

}
