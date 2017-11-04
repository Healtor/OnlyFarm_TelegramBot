package TLhello.TLhello;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {
	
	  public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	  public static final String MYSQL_URL = "jdbc:mysql://localhost/OnlyFarm"
	                                          + "user=root&password=root";

	
  public static void main(String[] args) {
      // Initialize Api Context
      ApiContextInitializer.init();

      // Instantiate Telegram Bots API
      TelegramBotsApi botsApi = new TelegramBotsApi();

   //   MySQL dao = new MySQL(MYSQL_DRIVER,MYSQL_URL);
      
      try {
	//	dao.readData();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      
      // Register our bot
      try {
          botsApi.registerBot(new OnlyFarm());
      } catch (TelegramApiException e) {
          e.printStackTrace();
      }
  }
  
}
