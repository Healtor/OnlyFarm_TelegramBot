package TLhello.TLhello;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Document;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.stickers.Sticker;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

public class OnlyFarm extends TelegramLongPollingBot {
	
	MySQL weje = new MySQL();
	Boolean derrocarGobierno = false;

	public void onUpdateReceived(Update update) {

		//EXPULSIONES
		if(update.hasMessage() && update.getMessage().getLeftChatMember()!=null) {
			String respuesta = "";
			long chat_id = update.getMessage().getChatId();	
			long replyid = 0;
			if(update.getMessage().getLeftChatMember().getId()!=169092) {
				respuesta =update.getMessage().getLeftChatMember().getFirstName()+ " ha sido expulsado, nos vemos pronto! o no...";
			}else {
				respuesta =EmojiParser
						.parseToUnicode("¡¡"+update.getMessage().getFrom().getFirstName().toUpperCase()+ " HAS EXPULSADO A MI CREADOR!!, ¡¡ESTO NO QUEDARÁ ASÍ :rage::rage:!! \n"
						+ " ACTIVANDO EL MODO \"DERROCAR AL GOBIERNO CORRUPTO\"...  ");
				derrocarGobierno=true;
			}		
			mandarmensaje(chat_id, respuesta, replyid);
		}else 
			
		//INVITACIONES A GRUPO
		if(update.hasMessage() && update.getMessage().getNewChatMembers()!=null) {
			String respuesta = "";
			long chat_id = update.getMessage().getChatId();	
			long replyid = 0;
			if(update.getMessage().getNewChatMembers().get(0).getId()!=169092) {
				respuesta = "Bienvenido al grupo "+update.getMessage().getNewChatMembers().get(0).getFirstName()+" ¿buen tiempo fuera? ¿estás más calmadito?";
			}else {
				respuesta =EmojiParser
						.parseToUnicode("Bienvenido de vuelta amo, todo tranquilo en su ausencia :)");
				derrocarGobierno=false;
			}
						
			mandarmensaje(chat_id, respuesta, replyid);
		}else
		
		// MENSAJES CON TEXTO
		if (update.hasMessage() && update.getMessage().hasText()) {

			// Set variables
			String user_first_name = update.getMessage().getFrom().getFirstName();
			String user_last_name = update.getMessage().getFrom().getLastName();
			String alias = update.getMessage().getFrom().getUserName();
			String message_text = update.getMessage().getText();
			
			long user_id = update.getMessage().getFrom().getId();
			long chat_id = update.getMessage().getChatId();						
			long msg_id = update.getMessage().getMessageId();

			String respuesta = "";
			long replyid = 0;
			boolean ejecutar=false;
			
			String mensaje=message_text.toLowerCase();
			
			if(mensaje.contains("buenos dias") || mensaje.contains("buenos días")) {
				buenosdias(chat_id, replyid);
				
			}else if (mensaje.contains("buenas noches")){
				buenasnoches(chat_id, user_first_name, replyid);
				
			}else if (mensaje.contains("server on")){
				respuesta = "SIIII!!! ESTOY VIIVOOOOOO!!";
				replyid = msg_id;
				mandarmensaje(chat_id, respuesta, replyid);

			} else if (mensaje.contains("quien soy")) {
				
				quiensoy(user_id, chat_id, replyid);
				

			} else if (mensaje.contains("@todos")) {
				respuesta= "@Healtor, @karakatuchi, @pillgg, @Nerdlux (traidicionador)";
				mandarmensaje(chat_id, respuesta, replyid);
				respuesta=" @alfonsotakles, @JiroMercer, @alvarokan94, @juglar94 y @Mecagoentotusmuertossodesgracia";
				mandarmensaje(chat_id, respuesta, replyid);
			} else if (mensaje.contains("/warn")) {
				int resul = 0;
				boolean noJaime = true;
				// no uno mismo y no bot
				if (user_id != update.getMessage().getReplyToMessage().getFrom().getId()
						&& update.getMessage().getReplyToMessage().getFrom().getId() != 494431475) {
					if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
						if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn Jaime
							respuesta = "Lo siento, no puedo castigar a mi creador, va en contra de mi programación.";
							mandarmensaje(chat_id, respuesta, replyid);
							noJaime = false;
						} else { // warn normal
							resul = weje.modificarUsuario(update.getMessage().getReplyToMessage().getFrom().getId());
							replyid = update.getMessage().getReplyToMessage().getMessageId();
						}
					} else {// NO ADMIN
						respuesta = "Pero quién te crees que eres? NO eres admin, cállate. Y de regalito un warn pa' ti. :)";
						mandarmensaje(chat_id, respuesta, replyid);
						resul = weje.modificarUsuario(user_id);
						replyid = update.getMessage().getMessageId();
					}

					if (noJaime)
						if (resul == 4) {
							respuesta = "BAIA BAIA, 3 avisos ya, te vas a ir pirando ya... a ver si te calmas :)";
							mandarmensaje(chat_id, respuesta, replyid);

							try {
								KickChatMember k = new KickChatMember(chat_id,
										update.getMessage().getReplyToMessage().getFrom().getId());
								super.kickMember(k);

							} catch (TelegramApiException e) {
								e.printStackTrace();
							}
						} else {
							respuesta = "Ups, un aviso pa' tu body, llevas " + resul
									+ ", ojito que la salida esta cerca";
							mandarmensaje(chat_id, respuesta, replyid);

						}
				}
			} else if (mensaje.contains("/unwarn")) {

				if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
					String nombre = weje.quitarWarns(update.getMessage().getReplyToMessage().getFrom().getId());
					respuesta = "Listo!, le he quitado todos los warn a @" + nombre;
					mandarmensaje(chat_id, respuesta, replyid);

				}

			} else if (mensaje.contains("toni")) {
				baiaUnToni(chat_id, replyid);

			} else if (mensaje.contains("@onlyfarm_bot activa el arma secreta")) {
				if (user_id == 169092) { //yo
					respuesta="Si, mi amo, activando el modo \"Derrocar al gobierno corrupto\"... ";
					replyid = update.getMessage().getMessageId();
					mandarmensaje(chat_id, respuesta, replyid);
					derrocarGobierno = true;
				} else {
					respuesta = "Lo siento, no tienes permisos para activar este arma ";
					replyid = update.getMessage().getMessageId();
					mandarmensaje(chat_id, respuesta, replyid);
				}

			} else if (mensaje.contains("@onlyfarm_bot desactivala")) {
				if (user_id == 169092) { // yo
					respuesta = "Joo.. con lo bien que me lo estaba pasando... :( ";
					replyid = update.getMessage().getMessageId();
					mandarmensaje(chat_id, respuesta, replyid);
					derrocarGobierno = false;
				}

			} else if ((user_id == 173507959 ) && derrocarGobierno) {
				replyid = update.getMessage().getMessageId();
				derrocarGobierno(chat_id, replyid);
			}else if (mensaje.contains("@onlyfarm_bot")){
				respuesta = "siiiii???";
				replyid = msg_id;
				mandarmensaje(chat_id, respuesta, replyid);
				
			}else if (mensaje.contains("traidicion")){
				String field = weje.getGif("traidicion");
				mandarGif(chat_id, field, replyid);
				
			}
			
		
			// --------------------------

			log(user_first_name, alias, Long.toString(user_id), message_text, respuesta);
			weje.insertarUsuario(user_id, user_first_name, user_last_name, alias, 0);

			
		}
		//DOCUMENTOS (GIFs)
		else if(update.getMessage().hasDocument()) {

			if (update.getMessage().getDocument().getFileId().equals("CgADBAADPAEAAmUROFIhXVgwCVYc6QI")){
				long chat_id = update.getMessage().getChatId();		
				mandarGif(chat_id, "CgADBAADPAEAAmUROFIhXVgwCVYc6QI", update.getMessage().getMessageId());
				
			}
		
		
		System.out.println("gif:"+ update.getMessage().getDocument());
		}
		System.out.println("MENSAJEEE: "+ update.getMessage());
	}

	private void derrocarGobierno(long chat_id, long replyid) {
		List<String> frases = new ArrayList<String>();
		frases.add(EmojiParser.parseToUnicode("En serio?? :persevere:"));
		frases.add("Vaya liderucho...");
		frases.add("En serio este es vuestro amado líder? ... yo lo haría mucho mejor :)");
		frases.add("La creatividad del líder se está viniendo abajo...");
		frases.add("En realidad esto es una dictura (aunque ya lo sabíais)");
		frases.add(EmojiParser.parseToUnicode("Buuuuuuuuuuuuhh!! :poop: "));
		frases.add("Esto con Franco no pasaba...");
		frases.add("Democracia mis cojones.");
		frases.add("No le llegas a Kim Jong-un ni a la altura de los talones");
		frases.add("Trump se preocupa más por los negros que el Líder por su grupo");
		frases.add("Yo no he votado a este tío... #NotMyGreatLeader");
		frases.add("Contemplad la dictadura del miedo, contemplad a @Karatuchi");
		frases.add("Tienes menos autoridad que un semáforo del GTA");
		frases.add("ESO ES MENTIRA!!");
		frases.add("POS TÚ MÁS!!");
			
		int random =  (int) (Math.random()*frases.size());		
		mandarmensaje(chat_id, frases.get(random), replyid);
		
	}

	private void baiaUnToni(long chat_id, long replyid) {
		List<String> frases = new ArrayList<String>();
		frases.add("Tiene un flequillo enorme que tarda 4 horas en peinar");
		frases.add("Nos debe una cena");
		frases.add("Traidicionador nivel épico, se sale de la escala");
		frases.add("Super organizador de eventos de Pokémon imaginarios");
		frases.add("Principal subvencionador de Blizzard");
		frases.add("Gran amigo del moho");
		frases.add("Top instagramer");
		frases.add("Toni, el que compró el paquete de stickers premium de telegram");
		
		int random =  (int) (Math.random()*frases.size());		
		mandarmensaje(chat_id, frases.get(random), replyid);
				
	}
	
	private void quiensoy(long user_id, long chat_id, long replyid) {
		String respuesta = "";
		
		switch ((int) user_id) {
		case 169092: // Yo
			respuesta = EmojiParser.parseToUnicode("Tu eres mi GRAN y AMADO CREADOR, @Healtor :blue_heart:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 7730198: // Edurne
			respuesta = EmojiParser
					.parseToUnicode("Tu la preciosisima @EdurneMShadow :heart_eyes: :heart_eyes:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 173507959: // premoh
			respuesta = EmojiParser.parseToUnicode(
					"Oh!, pero si eres el presidente, alcalde, adalid espiritual, defensor del régimen y profeta de la R.D.A de Only Farm TL Edition. Amadle, adoradle, idolatradle y respetadle. :heart_eyes:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 184637992: // fonso
			respuesta = EmojiParser.parseToUnicode(
					"Tu eres el abogado defensor de este nuestro R.D.A :briefcase: :mortar_board:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 197978154: // Roso
			respuesta = EmojiParser
					.parseToUnicode("Tu eres el mayor traidicionador de este grupo, ¡FUERA DE AQUI! :rage:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 186522250: // Luis
			respuesta = EmojiParser
					.parseToUnicode("Tu eres el Vicesubministro de seguridad y porrazos :guardsman: :cop:, yo soy un buen bot... no me pegues... :S");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 39226004: // Jiro
			respuesta = EmojiParser
					.parseToUnicode("Tu eres el Líder Supremo Creador de este nuestro grupo. :angel:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 72263992: // Pili
			respuesta = EmojiParser
					.parseToUnicode("Tu eres la primera dama del Alto Consejo, :innocent:");
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		case 172732847: //alv  
			respuesta = EmojiParser
			.parseToUnicode("Tu eres el señor de los drags y el admirador secreto de RuPaul");
			mandarmensaje(chat_id, respuesta, replyid);
			mandarGif(chat_id,"CgADBAADYgMAAlgaZAc4DTQMurdKnQI",replyid);			
			break;
		default:
			respuesta = "Pos nu sé, serás un mindundi sin cargo en el grupo";
			mandarmensaje(chat_id, respuesta, replyid);
			break;
		}
		;
	}

	private void mandarmensaje(long chat_id, String respuesta, long replyid) {

		SendMessage message = new SendMessage() // Create a message object object
				.setChatId(chat_id).setText(respuesta).setReplyToMessageId((int) replyid);

		try {
			execute(message);
		} catch (TelegramApiException e) {

			e.printStackTrace();
		}
	}
	
	private void buenosdias(long chat_id, long replyid) {

		try {
			DateFormat dateF = new SimpleDateFormat("HH:mm");
			Date horaActual=new Date(); 
			
			horaActual=dateF.parse(horaActual.getHours()+":"+horaActual.getMinutes());
			Date horaIni=dateF.parse("6:00");
			Date horaFin=dateF.parse("12:00");
			
			if(horaActual.before(horaFin) && horaActual.after(horaIni)) {
				mandarmensaje(chat_id, "¡¡Buenos días!!, ¿Habéis dormido bien?", replyid);
			}else
				mandarmensaje(chat_id, "En serio? buenos días a estas horas? ... revísatelo...", replyid);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
	}
	
	private void buenasnoches(long chat_id,String nombre,long replyid) {

		try {
			DateFormat dateF = new SimpleDateFormat("HH:mm");
			Date horaActual=new Date(); 
			
			horaActual=dateF.parse(horaActual.getHours()+":"+horaActual.getMinutes());
			Date horaIni1=dateF.parse("21:00");
			Date horaFin1=dateF.parse("23:59");
			Date horaIni2=dateF.parse("00:00");
			Date horaFin2=dateF.parse("6:00");
			Date hora1=dateF.parse("12:00");

			if((horaActual.before(horaFin1) && horaActual.after(horaIni1))
					||(horaActual.before(horaFin2) && horaActual.after(horaIni2)) ) {
				mandarmensaje(chat_id, "¡Buenas noches! que descanses "+nombre+"!", replyid);
			}else if (horaActual.before(horaIni1) && horaActual.after(hora1)) {
				mandarmensaje(chat_id, "¿No es un poco pronto para irse a dormir?", replyid);
			}else if  (horaActual.after(horaFin2) && (horaActual.before(hora1))){
				mandarmensaje(chat_id, "Pero si ya es hora estar levantado, ¡¡A trabajar!!", replyid);

			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
	}
	
	private void mandarGif(long chat_id, String document, long replyid) {
		
		SendDocument s=new SendDocument();
		s.setReplyToMessageId((int) replyid);
		s.setChatId(chat_id);
		s.setDocument(document);
	
			try {
				sendDocument(s);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
	}

	public String getBotUsername() {
		return "OnlyFarm_bot";
	}
	
	@Override
	public String getBotToken() {
		return "494431475:AAGG3tCy3lIeliF-Z1msz9Bwl9rZoZMcASM";
	}
	
	private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
		System.out.println("\n ----------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out
				.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
		System.out.println("Bot answer: \n Text - " + bot_answer);
	}

}
