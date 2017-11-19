package TLhello.TLhello;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import org.telegram.telegrambots.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

public class OnlyFarm extends TelegramLongPollingBot {

	MySQL weje = new MySQL();
	Boolean derrocarGobierno = false;
	long idExpulsor=0;
	Date bdias = new Date();
	Date bnoches = new Date();

	public void onUpdateReceived(Update update) {
		
		
		
		//System.out.println(update.getMessage());
		
		Long l=update.getMessage().getChatId();

		if (l == -1001193270199L || l == -1001118254446L) {
			
			
			// EXPULSIONES
			if (update.hasMessage() && update.getMessage().getLeftChatMember() != null) {
				String respuesta = "";
				long chat_id = update.getMessage().getChatId();
				long replyid = 0;
				if (update.getMessage().getLeftChatMember().getId() != 169092) {
					respuesta = update.getMessage().getLeftChatMember().getFirstName()
							+ " ha sido expulsado, nos vemos pronto! o no...";
				} else {
					respuesta = EmojiParser
							.parseToUnicode("¡¡" + update.getMessage().getFrom().getFirstName().toUpperCase()
									+ " HAS EXPULSADO A MI CREADOR!!, ¡¡ESTO NO QUEDARÁ ASÍ :rage::rage:!! \n"
									+ " ACTIVANDO EL MODO \"DERROCAR AL GOBIERNO CORRUPTO\"...  ");
					idExpulsor=update.getMessage().getFrom().getId();
					derrocarGobierno = true;
				}
				mandarmensaje(chat_id, respuesta, replyid);
			} else

			// INVITACIONES/ENTRADAS A GRUPO
			if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
				String respuesta = "";
				long chat_id = update.getMessage().getChatId();
				long replyid = 0;
				if (update.getMessage().getNewChatMembers().get(0).getId() != 169092) {
					respuesta = "Hola " + update.getMessage().getNewChatMembers().get(0).getFirstName()
							+ ", bienvenido a la República Democrática Autocrática de Only Farm TL Edition, presidida por nuestro querido y glorioso Gran Líder Karakatuchi. Si eres nuevo en el grupo, jura cumplir con nuestra constitución, proteger la bandera ante la adversidad y servir fielmente desde ahora y para siempre. No traidiciones porque el Gran Líder tiene el dedo de echar gente calentito. Si habías sido expulsado y ahora metido de nuevo en el grupo, no es porque te echemos de menos, seguro que es porque nos has dado pena con esa cara que tienes.";
				} else {
					respuesta = EmojiParser
							.parseToUnicode("Bienvenido de vuelta amo, todo tranquilo en su ausencia :)");
					derrocarGobierno = false;
				}

				mandarmensaje(chat_id, respuesta, replyid);
			} else

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

				String mensaje = message_text.toLowerCase();

				if (mensaje.contains("buenos dias") || mensaje.contains("buenos días")) {
					buenosdias(chat_id, replyid, user_id,alias);

				} else if (mensaje.contains("buenas noche")) {
					buenasnoches(chat_id, user_first_name, replyid, user_id);

				} else if (mensaje.contains("server on")) {
					respuesta = "SIIII!!! ESTOY VIIVOOOOOO!!";
					replyid = msg_id;
					mandarmensaje(chat_id, respuesta, replyid);

				} else if (mensaje.contains("quien soy") || mensaje.contains("quién soy")) {

					quiensoy(user_id, chat_id, replyid,alias);
				} else if (mensaje.contains("quien es @") || mensaje.contains("quién es @")) {
					quienEsAlias(update);
				} else if (mensaje.contains("quien es") || mensaje.contains("quién es")) {
					quienEsReply(update.getMessage().getReplyToMessage().getFrom().getId(), chat_id, replyid, update.getMessage().getReplyToMessage().getFrom().getUserName());
					
				} else if (mensaje.contains("@todos")) {
					respuesta = "@Healtor, @karakatuchi, @pillgg, @Nerdlux (traidicionador), @Marcitas, @EdurneMShadow";
					mandarmensaje(chat_id, respuesta, replyid);
					respuesta = " @alfonsotakles, @JiroMercer, @alvarokan94, @juglar94 y @Mecagoentotusmuertossodesgracia";
					mandarmensaje(chat_id, respuesta, replyid);
					
				} else if (mensaje.contains("/warn")) {
					int resul = 0;
					boolean noJaime = true;
					// no uno mismo y no bot
					if (update.getMessage().getReplyToMessage() != null) {
						if (user_id != update.getMessage().getReplyToMessage().getFrom().getId()
								&& update.getMessage().getReplyToMessage().getFrom().getId() != 494431475) {
							if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
								if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn Jaime
									respuesta = "Lo siento, no puedo castigar a mi creador, va en contra de mi programación.";
									mandarmensaje(chat_id, respuesta, replyid);
									noJaime = false;
								} else { // warn normal
									System.out.println(update.getMessage().getReplyToMessage().getFrom().getId());
									resul = weje.modificarUsuario(
											update.getMessage().getReplyToMessage().getFrom().getId());
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

									expulsar(chat_id, update.getMessage().getReplyToMessage().getFrom().getId());
									
									mandarmensaje(update.getMessage().getReplyToMessage().getFrom().getId(),
											"Pues nada, pareces que no aprendes pedazo de merluzo. Si crees que no te merecías este ban, habla al Gran Líder @Karakatuchi y díselo. Pero no le des la turra, que de dar pena a ser irritante hay una línea muy fina.",
											0);

								} else {
									respuesta = "Ups, un aviso pa' tu body, llevas " + resul
											+ ", ojito que la salida esta cerca";
									mandarmensaje(chat_id, respuesta, replyid);
									
									switch (resul) {
									case 1:
										mandarmensaje(update.getMessage().getReplyToMessage().getFrom().getId(),
												"Te tengo fichado, no digas más tonterías como esa porque puede ser que te ganes otro. GLK.",
												0);
										break;
									case 2:
										mandarmensaje(update.getMessage().getReplyToMessage().getFrom().getId(),
												"Te lo dije.", 0);
										break;

									}
									;

								}
						}
					}else
					mandarmensaje(chat_id, "Responde a un mensaje primero", replyid);
				} else if (mensaje.contains("/kick")) {
					if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
						if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn Jaime
							respuesta = "Lo siento, no puedo expulsar a mi creador, va en contra de mi programación.";
							mandarmensaje(chat_id, respuesta, replyid);
						} else 
							expulsar(chat_id, update.getMessage().getReplyToMessage().getFrom().getId());
					}else {
						mandarmensaje(chat_id, "Venga, hasta lue!", replyid);
						expulsar(chat_id, user_id);
					}
					
					
				} else if (mensaje.contains("/unwarn")) {

					if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
						String nombre = weje.quitarWarns(update.getMessage().getReplyToMessage().getFrom().getId());
						respuesta = "Listo!, le he quitado todos los warn a @" + nombre;
						mandarmensaje(chat_id, respuesta, replyid);
					}

				} else if (mensaje.contains("gobierno") || mensaje.contains("regimen")|| mensaje.contains("régimen")) {
					regimen(chat_id, replyid);
				} else if (mensaje.contains("/roll")) {
					dados(chat_id, replyid, message_text);
				} else if (mensaje.contains(" toni") || mensaje.contains(" toni ") || mensaje.contains("toni ") || (mensaje.startsWith("toni")&&mensaje.endsWith("toni"))) {
					baiaUnToni(chat_id, replyid);

				} else if (mensaje.contains("@onlyfarm_bot activa el arma secreta")) {
					if (user_id == 169092) { // yo
						respuesta = "Si, mi amo, activando el modo \"Derrocar al gobierno corrupto\"... ";
						replyid = update.getMessage().getMessageId();
						mandarmensaje(chat_id, respuesta, replyid);
						idExpulsor=173507959;
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

				} else if ((user_id == 173507959) && derrocarGobierno) {
					replyid = update.getMessage().getMessageId();
					derrocarGobierno(chat_id, replyid);
					
				} else if (mensaje.contains("/report")) {
					if(update.getMessage().getReplyToMessage()!=null) {
						//Jaime	
						mandarmensaje(169092,"@"+alias +" ha reportado un mensaje de @"+update.getMessage().getReplyToMessage().getFrom().getUserName()+", ha dicho esto:", replyid);
						mandarmensaje(169092, "\""+update.getMessage().getReplyToMessage().getText()+"\"", replyid);
						//karaka
						mandarmensaje(173507959,"@"+alias +" ha reportado un mensaje de @"+update.getMessage().getReplyToMessage().getFrom().getUserName()+", ha dicho esto:", replyid);
						mandarmensaje(173507959, "\""+update.getMessage().getReplyToMessage().getText()+"\"", replyid);
					}else 
						mandarmensaje(chat_id, "Responde a un mensaje primero", replyid);
				} else if (mensaje.contains("@onlyfarm_bot")) {
					respuesta = "siiiii???";
					replyid = msg_id;
					mandarmensaje(chat_id, respuesta, replyid);

				} else if (mensaje.contains("traidicion")) {
//					String field = weje.getGif("traidicion");
					mandarGif(chat_id, "CgADBAADTQEAAi85iVA52CLrlFIVHQI", replyid);
				} else if (mensaje.contains("pollo")) {
					mandarSticker(chat_id, "CAADBAADiAAD70_CCQQ61-IL2XAyAg");
				} else if (mensaje.contains("caña")) {
					mandarSticker(chat_id, "CAADBAADqgAD70_CCU0HD5j9qa24Ag");
				} else if (mensaje.contains("calmarno")) {
					mandarSticker(chat_id, "CAADBAADUQEAAtoAAQ4JYteU7EX3eYgC");
				} else if (mensaje.contains("toalla")) {
					mandarSticker(chat_id, "CAADBAADZwUAAo_pZgABq4hyxmbzVbwC");
				} else if (mensaje.contains("!constitucion") ||mensaje.contains("!constitución")) {
					mandarmensaje(chat_id, "Va, te la mando por privado", replyid);
					mandarGif(user_id,"BQADBAADSAIAArPwEFDjXRY_HxBvCgI", replyid);
				} else if (mensaje.contains("!bandera")) {	
					mandarFoto(chat_id, "AgADBAADZ6sxG7PwEFA7TKEWhp9SkElf4xkABPjtrOTuVmRWqc0DAAEC", replyid);
				} else if (mensaje.contains("!lider")) {	
					mandarFoto(chat_id, "AgADBAADaqsxG7PwEFCiUpZrquc8IBXg-RkABDBdSopSQpDaV-YCAAEC", replyid);
				} else if (mensaje.contains("!sello")) {	
					mandarFoto(chat_id, "AgADBAADBKsxG1QWOFBhEHvSe44Vbo9d4xkABKv2ZBkHixlrLvoDAAEC", replyid);
				
				} else if (mensaje.contains("!estadisticas")||mensaje.contains("!estadísticas")) {	
					respuesta =EmojiParser.parseToUnicode(weje.estadisticas());
					mandarmensaje(chat_id, respuesta, replyid);
				}
			
				// --------------------------

				log(user_first_name, alias, Long.toString(user_id), message_text, respuesta);
				weje.insertarUsuario(user_id, user_first_name, user_last_name, alias, 0);

			}
			// DOCUMENTOS (GIFs)
			else if (update.getMessage().hasDocument()) {

				if (update.getMessage().getDocument().getFileId().equals("CgADBAADPAEAAmUROFIhXVgwCVYc6QI")) {
					long chat_id = update.getMessage().getChatId();
					mandarGif(chat_id, "CgADBAADPAEAAmUROFIhXVgwCVYc6QI", update.getMessage().getMessageId());

				}

				System.out.println("gif:" + update.getMessage().getDocument());
			}
			System.out.println("MENSAJE: " + update.getMessage());

		}else if (l == 169092) {
			
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

			String mensaje = message_text.toLowerCase();

			if (mensaje.contains("dame el link")) {
				añadir();

			}
		}

	}

	private void derrocarGobierno(long chat_id, long replyid) {
		if(idExpulsor==173507959) { //Karaka
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
			frases.add("\"Constitución\", también conocida como \"El libro del dictador\"");
				
			int random =  (int) (Math.random()*frases.size());		
			mandarmensaje(chat_id, frases.get(random), replyid);
			
		}else if(idExpulsor==39226004) { //Jairo
			List<String> frases = new ArrayList<String>();
			frases.add(EmojiParser.parseToUnicode("En serio?? :persevere:"));
			frases.add("Vaya creador...");
			frases.add("Podríamos crear otro grupo sin este tio...");
			frases.add("Este tio es otro dictador en la sombra...");
			frases.add("lalalalalala");
			frases.add("Seguro que ahora me va a expulsar... es un chulo. ");
		
			int random =  (int) (Math.random()*frases.size());		
			mandarmensaje(chat_id, frases.get(random), replyid);
		}
		
		
	}
	private void dados(long chat_id, long replyid, String text) {

		StringTokenizer tokens=new StringTokenizer(text, " ");
		int dados=0;
		int tamaño=0;
		
		while(tokens.hasMoreTokens()){
			tokens.nextToken();
			StringTokenizer tokens2=new StringTokenizer(tokens.nextToken(), "d");
			dados=Integer.valueOf(tokens2.nextToken());
			tamaño=Integer.valueOf(tokens2.nextToken());
		}
		
		int numero=0;
		int total=0;
		ArrayList<Integer> n=new ArrayList<Integer>();
		for (int i = 0; i < dados; i++) {
			numero =  (int) (Math.random()*tamaño)+1;
			n.add(numero);
			total=total+numero;
		}
		String mensaje="Resultado: "+total+"\n";
		if(n.size()>1) {
			for (int i = 0; i < n.size(); i++) {
			
				if(i==n.size()-1) {
					mensaje=mensaje+n.get(i);
				}else
					mensaje=mensaje+n.get(i)+"-";
			}
		}
		
		mandarmensaje(chat_id, mensaje, replyid);
		
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
		frases.add("Campeón del mundo en llegar tarde");
		frases.add("El traidicionador exiliado");
		frases.add("Limpia la cocina de una vez, por favor.");
		frases.add("Amigo de sus amigos, aunque nuestro ya no.");
		
		
		int random =  (int) (Math.random()*frases.size());		
		mandarmensaje(chat_id, frases.get(random), replyid);
				
	}
	
	private void regimen(long chat_id, long replyid) {
		List<String> frases = new ArrayList<String>();
		frases.add("Como la R.D.A de Only Farm encabezada por el Gran Líder Karakatuchi no hay nada.");
		frases.add("Para gobierno bueno el del Gran Líder Karakatuchi, fua que bien lo hace el cabrón.");
		frases.add("He oido por ahí que dentro de poco en la R.D.A habrá elecciones, pero dificil veo yo que alguien HUMANO sea capaz de derrotar al peaso de líder que tienen. Es un fiera el tío");
		frases.add("Pero seguro que ese del que hablas no tiene una constitución tan buena como la que creó nuestro Gran Líder Karakatuchi para la R.D.A de Only Farm.");
		
		
		int random =  (int) (Math.random()*frases.size());		
		mandarmensaje(chat_id, frases.get(random), replyid);
				
	}
	private void quiensoy(long user_id, long chat_id, long replyid, String alias) {

		Usuario u = weje.buscarUsuario(alias);
		String mensaje = ".... nadie";

		mensaje = EmojiParser.parseToUnicode("Tu eres " + u.getDescripcion());

		mandarmensaje(chat_id, mensaje, replyid);

		switch ((int) user_id) {

		case 172732847: // alv
			mandarGif(chat_id, "CgADBAADYgMAAlgaZAc4DTQMurdKnQI", replyid);
			break;

		};
	}
	
	private void quienEsAlias(Update update) {
		
		StringTokenizer tokens=new StringTokenizer(update.getMessage().getText(), " ");
		
		while(tokens.hasMoreTokens()){
			String str=tokens.nextToken();
			if(str.startsWith("@")) {
				Usuario u = weje.buscarUsuario(str.substring(1));
				String mensaje = ".... nadie";

				mensaje = EmojiParser.parseToUnicode("Es " + u.getDescripcion());

				mandarmensaje(update.getMessage().getChatId(), mensaje, 0);
			}
		}
		
	}
	
	private void report(Update update) {
		//TODO
		StringTokenizer tokens=new StringTokenizer(update.getMessage().getText(), " ");
		
		while(tokens.hasMoreTokens()){
			String str=tokens.nextToken();
			if(str.startsWith("@")) {
				Usuario u = weje.buscarUsuario(str.substring(1));
				String mensaje = ".... nadie";

				mensaje = EmojiParser.parseToUnicode("Es " + u.getDescripcion());

				mandarmensaje(update.getMessage().getChatId(), mensaje, 0);
			}
		}
		
	}
	
	private void quienEsReply(long user_id, long chat_id, long replyid, String alias) {
		
		Usuario u = weje.buscarUsuario(alias);
		String mensaje = "";

		mensaje = EmojiParser.parseToUnicode("Es " + u.getDescripcion());

		mandarmensaje(chat_id, mensaje, replyid);

		switch ((int) user_id) {

		case 172732847: // alv
			mandarGif(chat_id, "CgADBAADYgMAAlgaZAc4DTQMurdKnQI", replyid);
			break;

		};
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
	
	private void expulsar(long chat_id, long id) {
		
		try {
			KickChatMember k = new KickChatMember(chat_id,(int) id);
			super.kickMember(k);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private void añadir() {
		
		try {
			ExportChatInviteLink ci = new ExportChatInviteLink();
			ci.setChatId(-1001118254446L);
			
			String s=super.exportChatInviteLink(ci);
			mandarmensaje(169092, s, 0);

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void mandarSticker(long chat_id, String sticker) {
		SendSticker s = new SendSticker();
		s.setChatId(chat_id);
		s.setSticker(sticker);
		try {
			super.sendSticker(s);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void buenosdias(long chat_id, long replyid, long userid,String alias) {
		Date horaActual = new Date();
		DateFormat dateF = new SimpleDateFormat("HH:mm");
		try {
			bdias=dateF.parse(bdias.getHours() + ":" + bdias.getMinutes());
			horaActual = dateF.parse(horaActual.getHours() + ":" + horaActual.getMinutes());
			System.out.println(horaActual.getTime() -bdias.getTime() );
		//	if (horaActual.getTime() -bdias.getTime() >= 10*60*1000) {

				Date horaIni = dateF.parse("6:00");
				Date horaFin = dateF.parse("12:00");

				if (horaActual.before(horaFin) && horaActual.after(horaIni)) {
					if(userid==173507959) { //GLK
						mandarSticker(chat_id,"CAADBAADjgAD70_CCUCbuRnXNN7uAg");
					}else
					mandarmensaje(chat_id, "¡¡Buenos días @"+ alias+"!!, ¿Has dormido bien? :)", replyid);
					//mandarGif(chat_id,"CgADAQADsgADEH4SDZPeelLNvJZCAg",replyid);
				} else
					mandarmensaje(chat_id, "En serio? buenos días a estas horas? ... revísatelo...", replyid);

				bdias = horaActual;
		//	}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}
	
	private void buenasnoches(long chat_id,String nombre,long replyid, long id) {

		try {
			DateFormat dateF = new SimpleDateFormat("HH:mm");
			Date horaActual=new Date(); 
			bnoches=dateF.parse(bnoches.getHours() + ":" + bnoches.getMinutes());
			horaActual=dateF.parse(horaActual.getHours()+":"+horaActual.getMinutes());
			Date horaIni1=dateF.parse("21:00");
			Date horaFin1=dateF.parse("23:59");
			Date horaIni2=dateF.parse("00:00");
			Date horaFin2=dateF.parse("6:00");
			Date hora1=dateF.parse("12:00");
			
			System.out.println(horaActual.getTime() -bnoches.getTime() );

			//if(horaActual.getTime() -bnoches.getTime() >= 10*60*1000) {
				
				if((horaActual.before(horaFin1) && horaActual.after(horaIni1))
						||(horaActual.before(horaFin2) && horaActual.after(horaIni2)) ) {
					if(id!=72263992) {
						mandarmensaje(chat_id, "¡Buenas noches! Que descanses "+nombre+"!", replyid);
					}else {
						List<String> frases = new ArrayList<String>();
						frases.add("Ya pensaba que no me ibas a decir nada "+nombre+"... Que descanses!!! :)");
						frases.add(EmojiParser.parseToUnicode("Espero que sueñes conmigo! :smirk:"));
						frases.add(EmojiParser.parseToUnicode("Ojalá pudiera ser human@ para irme a dormir contigo :kissing:"));
						frases.add(EmojiParser.parseToUnicode("Como me gusta darte las buenas noches todos los días, que descanses!"));
						frases.add(EmojiParser.parseToUnicode("Las noches son demasiado largas cuando estás lejos de mi... Buenas noches... :cry:"));
						
						
						int random =  (int) (Math.random()*frases.size());		
						mandarmensaje(chat_id, frases.get(random), 72263992);
						
						mandarGif(chat_id, "CgADBAADXAMAAjUZZAfk7Q1aA0kstQI", replyid);
						
					}
					
				}else if (horaActual.before(horaIni1) && horaActual.after(hora1)) {
					mandarmensaje(chat_id, "¿No es un poco pronto para irse a dormir?", replyid);
				}else if  (horaActual.after(horaFin2) && (horaActual.before(hora1))){
					mandarmensaje(chat_id, "Pero si ya es hora estar levantado, ¡¡A trabajar!!", replyid);

				}
				
				bnoches = horaActual;
			//}
			
		
			
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

	private void mandarFoto(long chat_id, String document, long replyid) {
		
		SendPhoto ph=new SendPhoto();
		ph.setPhoto(document);
		ph.setChatId(chat_id);
		try {
			super.sendPhoto(ph);
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
