package OF_Bot;

import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Evento extends Thread {
	long tiempo;
	OnlyFarm of;
	String mensaje;

	
	public Evento(long time, OnlyFarm of,String msg) {
		
		this.of=of;
		mensaje = msg;	
		tiempo=time;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(tiempo);

			mandarmensaje(-1001193270199L, mensaje, 0);
			
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	
	private void mandarmensaje(long chat_id, String respuesta, long replyid) {

		SendMessage message = new SendMessage() // Create a message object object
				.setChatId(chat_id).setText(respuesta).setReplyToMessageId((int) replyid);

		try {
			of.execute(message);
		} catch (TelegramApiException e) {

			e.printStackTrace();
		}
	}
}
