package OF_Bot;

import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Temporizador extends Thread {
	long tiempo;
	OnlyFarm of;
	Update update;
	long h,m,s;
	String mensaje;

	
	public Temporizador(String h, String m, String s, Update u, OnlyFarm of,String msg) {
		
		update=u;
		this.of=of;
		mensaje = msg;

		this.h= Integer.parseInt(h);
		this.m= Integer.parseInt(m);
		this.s= Integer.parseInt(s);
			
		tiempo= TimeUnit.HOURS.toMillis(this.h) +  TimeUnit.MINUTES.toMillis(this.m)+ TimeUnit.SECONDS.toMillis(this.s);
		System.out.println(tiempo);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(tiempo);
			String mensa="";
			if(mensaje=="no") {
			mensa="@"+update.getMessage().getFrom().getUserName()+" ya ha pasado el tiempo!! (";
			if(h!=0)
				mensa=mensa+h+"h ";			
			if(m!=0)
				mensa=mensa+m+"min ";
			if(s!=0)
				mensa=mensa+s+"seg";
			
			mensa=mensa+")";
			}else
				mensa=mensaje;
			
			mandarmensaje(update.getMessage().getChatId(), mensa, 0);
			mandarmensaje(update.getMessage().getFrom().getId(), mensa, 0);
			
			
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
