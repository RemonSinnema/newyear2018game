package name.sinnema.game.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import name.sinnema.game.server.GameController;


@SpringBootApplication(scanBasePackageClasses = { TicTacToeApplication.class, GameController.class })
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }

  @Bean
  public TicTacToe game() {
    return new TicTacToe();
  }

}
