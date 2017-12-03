package TLhello.TLhello;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class OFMain {
		
  public static void main(String[] args) {
      // Initialize Api Context
      ApiContextInitializer.init();

      // Instantiate Telegram Bots API
      TelegramBotsApi botsApi = new TelegramBotsApi();
   
      try {

	} catch (Exception e1) {	
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
