package OF_Bot;

import static java.lang.Math.toIntExact;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

public class OnlyFarm extends TelegramLongPollingBot {

	MySQL weje = new MySQL();
	Boolean derrocarGobierno = false;
	long idExpulsor = 0;
	Date bdias = new Date();
	Date bnoches = new Date();
	int ComandoFase = 0;
	Comando c = new Comando();

	public void onUpdateReceived(Update update) {

		Long l = null;
		System.out.println(update);

		if (!update.hasCallbackQuery() && update.hasMessage()) {
			l = update.getMessage().getChatId();
		} else
			l = update.getCallbackQuery().getMessage().getChatId();

		if (l == -1001193270199L || l == -1001118254446L) { // Grupos PRUEBA y OF, respectivamente

			// EXPULSIONES Y ABANDONOS
			if (update.hasMessage() && update.getMessage().getLeftChatMember() != null) {
				String respuesta = "";
				long chat_id = update.getMessage().getChatId();
				long replyid = 0;

				if (update.getMessage().getLeftChatMember().getId().compareTo(update.getMessage().getFrom().getId()) != 0) { //expulsion

					weje.expulsar(update.getMessage().getLeftChatMember().getId());

					if (update.getMessage().getLeftChatMember().getId() != 169092) { //Si no es Jaime
						respuesta = update.getMessage().getLeftChatMember().getFirstName()
								+ " ha sido expulsado, nos vemos pronto! o no...";
					} else { // JAIME
						respuesta = EmojiParser
								.parseToUnicode("¡¡" + update.getMessage().getFrom().getFirstName().toUpperCase()
										+ " HAS EXPULSADO A MI CREADOR!!, ¡¡ESTO NO QUEDARÁ ASÍ :rage::rage:!! \n"
										+ " ACTIVANDO EL MODO \"DERROCAR AL GOBIERNO CORRUPTO\"...  ");
						idExpulsor = update.getMessage().getFrom().getId();
						derrocarGobierno = true;
					}
					
					mandarmensaje(chat_id, respuesta, replyid);
				} else { //abandono
					weje.abandono(update.getMessage().getLeftChatMember().getId());
					mandarmensaje(chat_id, "Pues nada... que se pira... A mandar reply le toca...", replyid);
				}
			} else

			// INVITACIONES/ENTRADAS A GRUPO
			if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
				String respuesta = "";
				long chat_id = update.getMessage().getChatId();
				long replyid = 0;
				if (update.getMessage().getNewChatMembers().get(0).getId() != 169092) { //Si no Jaime
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

				/** ----------- RESPUESTAS ----------- */	
				if (mensaje.contains("buenos dias") || mensaje.contains("buenos días")) {
					buenosdias(chat_id, replyid, user_id, alias);

				} else if (mensaje.contains("buenas noche")) {
					buenasnoches(chat_id, user_first_name, replyid, user_id);
					
				} else if (mensaje.contains("/time")) {
					time(update);

				} else if (comprobarComando(chat_id, mensaje, replyid)) {

				} else if (mensaje.contains("server on")) {
					mandarmensaje(chat_id, "SIIII!!! ESTOY VIIVOOOOOO!!", msg_id);
					
				} else if (mensaje.contains("@onlyfarm_bot")) {
					mandarmensaje(chat_id,  "siiiii???", msg_id);
					
				} else if (mensaje.contains("quien soy") || mensaje.contains("quién soy")) {
					quiensoy(user_id, chat_id, replyid, alias);
				} else if (mensaje.contains("quien es @") || mensaje.contains("quién es @")) {
					quienEsAlias(update);
				} else if (mensaje.contains("quien es") || mensaje.contains("quién es")) {
					if (update.getMessage().getReplyToMessage() != null)
						quienEsReply(update.getMessage().getReplyToMessage().getFrom().getId(), chat_id, replyid,
								update.getMessage().getReplyToMessage().getFrom().getUserName());

				} else if (mensaje.contains("rupaul")) {
					RuPaul(chat_id, replyid);
					
				} else if (mensaje.contains("trust me")) {
					if (user_id == 169092 || user_id == 7730198 || user_id == 184637992) {
						mandarFoto(chat_id, "AgADBAADda0xG9dzuFBbeiDpkR7xYIzm-RkABA4T8GtTDdng7J8DAAEC", replyid);
					} else if (user_id == 39226004 || user_id == 72263992 || user_id == 188858869) {
						mandarFoto(chat_id, "AgADBAADdq0xG9dzuFBGKKFv01x-r_ouJhoABGRrgoDyGut_IHYAAgI", replyid);
					} else {
						mandarFoto(chat_id, "AgADBAADd60xG9dzuFDEU_OFdmQGGTNc4xkABIvt3r7LmnxZqIgEAAEC", replyid);
					}

				/** -----------MODERACIÓN----------- */	
					
				} else if (mensaje.contains("/warn")) { //---WARN---
					int resul = 0;
					boolean noJaime = true;
					long userKick = 0;
					
					if (update.getMessage().getReplyToMessage() != null) {
						if (user_id != update.getMessage().getReplyToMessage().getFrom().getId()
								&& update.getMessage().getReplyToMessage().getFrom().getId() != 494431475) { // no autowarn y no OF_Bot

							if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
								if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn a Jaime
									mandarmensaje(chat_id, "Lo siento, no puedo castigar a mi creador, va en contra de mi programación.", replyid);
									noJaime = false;
								} else { // warn normal
									resul = weje.modificarUsuario(update.getMessage().getReplyToMessage().getFrom().getId());
									replyid = update.getMessage().getReplyToMessage().getMessageId();
									userKick = update.getMessage().getReplyToMessage().getFrom().getId();
								}
								
							// NO ADMIN
							} else if (user_id == 197978154) { // nerdlux
								mandarmensaje(chat_id, "Que pesao eres @nerdlux, te voy a meter un warn pa' que dejes de tocar los huevos", replyid);
								resul = weje.modificarUsuario(user_id);
								userKick = user_id;

							} else {
								mandarmensaje(chat_id, "Pero quién te crees que eres? NO eres admin, cállate. Y de regalito un warn pa' ti. :)", replyid);
								resul = weje.modificarUsuario(user_id);
								userKick = user_id;
							}
							warn(noJaime, chat_id, msg_id, userKick, resul);
						}
					} else
						mandarmensaje(chat_id, "Responde a un mensaje primero", replyid);
					
				} else if (mensaje.contains("/unwarn")) { //---UNWARN---

					if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
						String nombre = weje.quitarWarns(update.getMessage().getReplyToMessage().getFrom().getId());
						mandarmensaje(chat_id, "Listo!, le he quitado todos los warn a @" + nombre, replyid);
					}else
						mandarmensaje(chat_id, "No tienes permiso para usar /unwarn", replyid);
					
				} else if (mensaje.contains("/kick")) { // ---KICK---
					if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
						if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn Jaime
							mandarmensaje(chat_id, "Lo siento, no puedo expulsar a mi creador, va en contra de mi programación.", replyid);
						} else
							expulsar(chat_id, update.getMessage().getReplyToMessage().getFrom().getId());
					} else {
						mandarmensaje(chat_id, "Intentando kickear sin ser admin...", replyid);
						int resul = weje.modificarUsuario(user_id);
						warn(true, chat_id, replyid, user_id, resul);
					}

				} else if (mensaje.contains("/mute")) { // ---MUTE---
					if (update.getMessage().getReplyToMessage() != null) {
						if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
							if (update.getMessage().getReplyToMessage().getFrom().getId() == 169092) { // warn Jaime
								mandarmensaje(chat_id, "Lo siento, mi creador puede decir lo que quiera...", replyid);
							} else { // mute normal
								mandarmensaje(chat_id, "Shhhh... estás molestando...", replyid);
								mute(chat_id, update.getMessage().getReplyToMessage().getFrom().getId());
							}
						}
					}
					
				} else if (mensaje.contains("/unmute")) { // ---UNMUTE---
					if (update.getMessage().getReplyToMessage() != null) {
						if (user_id == 169092 || user_id == 39226004 || user_id == 173507959) { // ADMIN
							mandarmensaje(chat_id, "Ya puedes hablar @"
									+ update.getMessage().getReplyToMessage().getFrom().getUserName(), replyid);
							unmute(chat_id, update.getMessage().getReplyToMessage().getFrom().getId());
						}
					}
					
				} else if (mensaje.contains("/report")) { // ---REPORT---
					report(update);
			
				} else if (mensaje.contains("@onlyfarm_bot activa el arma secreta")) { // ---ARMA SECRETA---

					if (user_id == 169092) { // JAIME
						mandarmensaje(chat_id, "Si, mi amo, activando el modo \\\"Derrocar al gobierno corrupto\\\"... ", msg_id);
						idExpulsor = 173507959;
						derrocarGobierno = true;
					} else {
						mandarmensaje(chat_id, "Lo siento, no tienes permisos para activar este arma ", msg_id);
					}

				} else if (mensaje.contains("@onlyfarm_bot desactivala")) { // ---/ARMA SECRETA---
					if (user_id == 169092) { // yo
						mandarmensaje(chat_id, "Joo.. con lo bien que me lo estaba pasando... :( ", msg_id);
						derrocarGobierno = false;
					}

				} else if ((user_id == 173507959 || user_id == 39226004) && derrocarGobierno) {
					derrocarGobierno(chat_id, msg_id);

		
				/** -----------/MODERACIÓN----------- */	
				
				/** ----------- FUNCIONES ----------- */
					
				} else if (mensaje.contains("/roll")) {
					dados(chat_id, replyid, message_text);
					
				} else if (mensaje.contains(" toni") || mensaje.contains(" toni ") || mensaje.contains("toni ")
						|| (mensaje.startsWith("toni") && mensaje.endsWith("toni"))) {
					baiaUnToni(chat_id, replyid);
			
				} else if (mensaje.contains("ancla esto")) {
					if (update.getMessage().getReplyToMessage() != null) {
						anclar(chat_id, update.getMessage().getReplyToMessage().getMessageId());
					} else {
						mandarmensaje(chat_id, "Responde primero al mensaje que quieras que ancle :)", replyid);
					}
				
				} else if (mensaje.contains("@todos")) {
					respuesta = "@Healtor, @karakatuchi, @pillgg, @Nerdlux (traidicionador), @EdurneMShadow";
					mandarmensaje(chat_id, respuesta, replyid);
					respuesta = " @alfonsotakles, @JiroMercer, @alvarokan94, @juglar94 y @Marcitas ";
					mandarmensaje(chat_id, respuesta, replyid);

				} else if (mensaje.contains("!estadisticas") || mensaje.contains("!estadísticas")) {
					respuesta = EmojiParser.parseToUnicode(weje.estadisticas());
					mandarmensaje(chat_id, respuesta, replyid);

				} else if (mensaje.contains("!constitucion") || mensaje.contains("!constitución")) {
					mandarGif(user_id, "BQADBAADSAIAArPwEFDjXRY_HxBvCgI", replyid);
					crearBotonesPrimero("Vale, te lo mando por privado ¿Alguién más la quiere?", chat_id, "Yo!","constitucion");

				} else if (mensaje.contains("/help")) {
					help(user_id, replyid);
					crearBotonesPrimero("Vale, te lo mando por privado ¿Alguién más lo quiere?", chat_id, "Yo!","help");

				} else if (mensaje.contains("me la pido") || mensaje.contains("me lo pido")) {
					crearBotonesPrimero("Pongamos orden de preferencias!!!\n" + alias, chat_id, "PA MIII!", "pami");

				}

				/** ----------- /FUNCIONES ----------- */

				log(user_first_name, alias, Long.toString(user_id), message_text, respuesta);
				
				if (l == -1001118254446L) //Actualizar número de mensajes
					weje.insertarUsuario(user_id, user_first_name, user_last_name, alias, 0);

			}
			/** ----------- CALLBACKQUERY ----------- */
			
			else if (update.hasCallbackQuery()) { 
				// Set variables
				String call_data = update.getCallbackQuery().getData();
				long message_id = update.getCallbackQuery().getMessage().getMessageId();
				long chat_id = update.getCallbackQuery().getMessage().getChatId();
				String answer = null;

				if (call_data.equals("pami")) {
					String m = update.getCallbackQuery().getMessage().getText();
					StringTokenizer st = new StringTokenizer(m, "\n");

					String alias = update.getCallbackQuery().getFrom().getUserName();
					String resultado = "";
					Boolean añadir = true;
					String t = "";
					while (st.hasMoreTokens()) {
						t = st.nextToken();
						if (t.contains(alias)) {
							añadir = false;

							AnswerCallbackQuery borrar = new AnswerCallbackQuery();
							borrar.setText("Pos ya no es pa' ti");
							borrar.setShowAlert(true);
							borrar.setCallbackQueryId(update.getCallbackQuery().getId());

							try {
								super.answerCallbackQuery(borrar);
							} catch (TelegramApiException e) {
								e.printStackTrace();
							}
						} else {
							resultado = resultado + "\n" + t;
						}
					}

					answer = resultado;

					if (añadir)
						answer = update.getCallbackQuery().getMessage().getText() + "\n" + alias + "\n";

					crearBotones(chat_id, message_id, answer, "PA MIII!", "pami");
					
				} else if (call_data.equals("help")) {
					Integer id = update.getCallbackQuery().getFrom().getId();
					help(id, 0);
					
				} else if (call_data.equals("constitucion")) {
					Integer id = update.getCallbackQuery().getFrom().getId();
					mandarGif(id, "BQADBAADSAIAArPwEFDjXRY_HxBvCgI", 0);
				}

			}
			// DOCUMENTOS (GIFs)
			else if (update.getMessage().hasDocument()) {
				long chat_id = update.getMessage().getChatId();
				long user_id = update.getMessage().getFrom().getId();

//				if (update.getMessage().getDocument().getFileId().equals("CgADBAADPAEAAmUROFIhXVgwCVYc6QI")) {
//					mandarGif(chat_id, "CgADBAADPAEAAmUROFIhXVgwCVYc6QI", update.getMessage().getMessageId());

				if (derrocarGobierno && user_id == 173507959) {
					mandarGif(chat_id, "CgADBAADPAEAAmUROFIhXVgwCVYc6QI", update.getMessage().getMessageId());
				}

			// MENSAJE DE VOZ
			} else if (update.getMessage().getVoice() != null) {
				long chat_id = update.getMessage().getChatId();
				long user_id = update.getMessage().getFrom().getId();

				if (derrocarGobierno && user_id == 173507959) {
					long replyid = update.getMessage().getMessageId();
					derrocarGobierno(chat_id, replyid);
				}
				
			// FOTOS
			} else if (update.getMessage().hasPhoto()) {
				long chat_id = update.getMessage().getChatId();
				long user_id = update.getMessage().getFrom().getId();
				
				if (derrocarGobierno && user_id == 173507959) { //
					long replyid = update.getMessage().getMessageId();
					mandarFoto(chat_id, "AgADBAADGqwxG8sdOVE_HCvr6TE5AnfEJxoABCPOLbMrrAsBcBMBAAEC", replyid);
				}
			}
			
		} else if (l == 169092) { //CHAT PRIVADO CON JAIME

			// VARIABLES
			String message_text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();
			long replyid = 0;
			String mensaje = null;
			
			if (message_text != null)
				mensaje = message_text.toLowerCase();

			switch (ComandoFase) {
			case 1:
				c.setComando(message_text);
				mandarmensaje(chat_id, "Ahora dime que quieres que responda", replyid);
				ComandoFase = 2;
				break;
			case 2:
				int n = 0;
				if (update.getMessage().hasDocument()) {
					c.setRespuesta(update.getMessage().getDocument().getFileId());
					c.setTipo("d");
					n = weje.insertarComando(c);
				} else if (update.getMessage().hasPhoto()) {
					c.setRespuesta(update.getMessage().getPhoto().get(0).getFileId());
					c.setTipo("f");
					n = weje.insertarComando(c);
				} else if (update.getMessage().hasText()) {
					c.setRespuesta(message_text);
					c.setTipo("m");
					n = weje.insertarComando(c);
				} else if (update.getMessage().getSticker() != null) {
					c.setRespuesta(update.getMessage().getSticker().getFileId());
					c.setTipo("s");
					n = weje.insertarComando(c);
				}

				if (n == 0) {
					mandarmensaje(chat_id, "¡Hecho! prueba el nuevo comando!", replyid);
				} else
					mandarmensaje(chat_id, "Error al crear el comando", replyid);

				ComandoFase = 0;
				break;
			}

			if (mensaje != null)
				if (mensaje.contains("dame el link")) {
					enlace();
					
				} else if (mensaje.contains("/comando")) {
					mandarmensaje(chat_id, "Vale, dime el nombre del comando", replyid);
					ComandoFase = 1;
					
				} else if (mensaje.contains("/delete")) {
					StringTokenizer tokens = new StringTokenizer(mensaje, " ");
					tokens.nextToken();
					int n = weje.borrarComando(tokens.nextToken());
					if (n == 0) {
						mandarmensaje(chat_id, "Comando borrado", replyid);
					} else
						mandarmensaje(chat_id, "Error al borrar el comando", replyid);
					
				} else if (mensaje.contains("lista de comandos completa")) {
					mandarmensaje(chat_id, weje.listarComandos(1), replyid);

				} else if (mensaje.contains("lista de comandos")) {
					mandarmensaje(chat_id, weje.listarComandos(2), replyid);
					
				} else if (mensaje.contains("/saypruebas")) {
					StringTokenizer tokens = new StringTokenizer(mensaje, " ");
					tokens.nextToken();
					mandarmensaje(-1001193270199L, tokens.nextToken("/n"), replyid);
					
				} else if (mensaje.contains("/say")) {
					StringTokenizer tokens = new StringTokenizer(mensaje, " ");
					tokens.nextToken();
					mandarmensaje(-1001118254446L, tokens.nextToken("/n"), replyid);
				}

			comprobarComando(chat_id, mensaje, replyid);

		}
	}
	

	private void unmute(long chat_id, Integer user_id) {
		RestrictChatMember r = new RestrictChatMember(chat_id, (int) user_id);
		r.setCanSendMediaMessages(true);
		r.setCanSendMessages(true);
		r.setCanSendOtherMessages(true);
		r.setCanAddWebPagePreviews(true);
		r.setUntilDate(5);
		try {
			super.restrictChatMember(r);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

	private void mute(long chat_id, long user_id) {

		RestrictChatMember r = new RestrictChatMember(chat_id, (int) user_id);
		r.setCanSendMediaMessages(false);
		r.setCanSendMessages(false);
		r.setCanSendOtherMessages(false);

		// Date myDate = new Date();
		// new SimpleDateFormat("HH:mm").format(myDate);

		r.setUntilDate(600000);
		try {
			super.restrictChatMember(r);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}

	private void report(Update update) {

		if (update.getMessage().getReplyToMessage() != null) {
			String alias = update.getMessage().getFrom().getUserName();
			// Jaime
			mandarmensaje(169092, "@" + alias + " ha reportado un mensaje de @"
					+ update.getMessage().getReplyToMessage().getFrom().getUserName() + ", ha dicho esto:", 0);
			mandarmensaje(169092, "\"" + update.getMessage().getReplyToMessage().getText() + "\"", 0);
			// karaka
			mandarmensaje(173507959, "@" + alias + " ha reportado un mensaje de @"
					+ update.getMessage().getReplyToMessage().getFrom().getUserName() + ", ha dicho esto:", 0);
			mandarmensaje(173507959, "\"" + update.getMessage().getReplyToMessage().getText() + "\"", 0);
		} else
			mandarmensaje(update.getMessage().getChatId(), "Responde a un mensaje primero", 0);

	}

	private void warn(Boolean noJaime, long chat_id, long replyid, long userKick, int resul) {
		String respuesta;
		if (noJaime)
			if (resul == 4) {
				respuesta = "BAIA BAIA, 3 avisos ya, te vas a ir pirando ya... a ver si te calmas :)";
				mandarmensaje(chat_id, respuesta, replyid);

				expulsar(chat_id, userKick);

				mandarmensaje(userKick,
						"Pues nada, pareces que no aprendes pedazo de merluzo. Si crees que no te merecías este ban, habla al Gran Líder @Karakatuchi y díselo. Pero no le des la turra, que de dar pena a ser irritante hay una línea muy fina.",
						0);

			} else {
				respuesta = "Ups, un aviso pa' tu body, llevas " + resul + ", ojito que la salida esta cerca";
				mandarmensaje(chat_id, respuesta, replyid);

				switch (resul) {
				case 1:
					mandarmensaje(userKick,
							"Te tengo fichado, no digas más tonterías como esa porque puede ser que te ganes otro. GLK.",
							0);
					break;
				case 2:
					mandarmensaje(userKick, "Te lo dije.", 0);
					break;

				}
				;

			}
	}

	private void expulsar(long chat_id, long id) {

		try {
			weje.expulsar(id);
			KickChatMember k = new KickChatMember(chat_id, (int) id);
			super.kickMember(k);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void time(Update update) {

		String text = update.getMessage().getText();

		// time 1h 2m 3s - MENSAJE
		StringTokenizer tokens = new StringTokenizer(text, " ");
		int n = tokens.countTokens();
		int k = 1;
		String mensaje = "no";

		String h = "0", m = "0", s = "0";
		tokens.nextToken();
		Boolean bh = true, bm = true, bs = true;
		Boolean mj = false;

		while (tokens.hasMoreTokens()) {
			String tiempos = tokens.nextToken();

			if (mj) {
				mensaje = mensaje + tiempos + " ";
			}

			if (tiempos.contains("-")) {
				mj = true;
				mensaje = "";

				bh = false;
				bm = false;
				bs = false;

			}

			// System.out.println("TIEMPOS "+tiempos.toString());
			if (tiempos.contains("h") && bh) {
				StringTokenizer hora = new StringTokenizer(tiempos, "h");

				if (hora.hasMoreTokens()) {
					h = hora.nextToken();
					// System.out.println(h);
					h.substring(h.length() - 1);
					bh = false;
				}
				// System.out.println("Hora: "+h);
			}

			if (tiempos.contains("m") && bm) {
				StringTokenizer min = new StringTokenizer(tiempos, "m");

				if (min.hasMoreTokens()) {
					m = min.nextToken();
					// System.out.println(m);
					m.substring(m.length() - 1);
					bm = false;
				}
				// System.out.println("Minutos: "+m);
			}

			if (tiempos.contains("s") && bs) {
				StringTokenizer seg = new StringTokenizer(tiempos, "s");

				if (seg.hasMoreTokens()) {
					s = seg.nextToken();
					// System.out.println(s);
					s.substring(s.length() - 1);
					bs = false;
				}
				// System.out.println("Segundos: "+s);
			}

		}

		if (n > 1) {
			mandarmensaje(update.getMessage().getChatId(), "Vale, ahora te aviso!", 0);
			Temporizador t = new Temporizador(h, m, s, update, this, mensaje);
			t.start();

		} else
			mandarmensaje(update.getMessage().getChatId(),
					"Mandame el tiempo en este formato: \"/time 1h 2m 3s - Mensaje\"", 0);

	}

	private void enlace() {
	
		try {
			ExportChatInviteLink ci = new ExportChatInviteLink();
			ci.setChatId(-1001118254446L);
	
			String s = super.exportChatInviteLink(ci);
			mandarmensaje(169092, s, 0);
	
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	private void anclar(long chat_id, long message_id) {
		PinChatMessage p = new PinChatMessage();
		p.setChatId(chat_id);
		p.setMessageId((int) message_id);
		try {
			super.pinChatMessage(p);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	private void dados(long chat_id, long replyid, String text) {
	
		StringTokenizer tokens = new StringTokenizer(text, " ");
		int dados = 0;
		int tamaño = 0;
	
		while (tokens.hasMoreTokens()) {
			tokens.nextToken();
			StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(), "d");
			dados = Integer.valueOf(tokens2.nextToken());
			tamaño = Integer.valueOf(tokens2.nextToken());
		}
		int numero = 0;
		int total = 0;
		ArrayList<Integer> n = new ArrayList<Integer>();
		if (tamaño <= 20 && dados <= 20) {
			if (tamaño == 3 || tamaño == 4 || tamaño == 6 || tamaño == 8 || tamaño == 10 || tamaño == 12
					|| tamaño == 20) {
	
				for (int i = 0; i < dados; i++) {
					numero = (int) (Math.random() * tamaño) + 1;
					n.add(numero);
					total = total + numero;
				}
				String mensaje = "Resultado: " + total + "\n";
				if (n.size() > 1) {
					for (int i = 0; i < n.size(); i++) {
	
						if (i == n.size() - 1) {
							mensaje = mensaje + n.get(i);
						} else
							mensaje = mensaje + n.get(i) + "-";
					}
				}
	
				mandarmensaje(chat_id, mensaje, replyid);
			} else
				mandarmensaje(chat_id, "Solo puedes tirar hasta 20 dados de 3-4-6-8-10-12-20", replyid);
	
		} else
			mandarmensaje(chat_id, "Solo puedes tirar hasta 20 dados de 3-4-6-8-10-12-20", replyid);
	}


	private void quiensoy(long user_id, long chat_id, long replyid, String alias) {
	
		Usuario u = weje.buscarUsuario(alias);
		String mensaje = ".... nadie";
	
		mensaje = EmojiParser.parseToUnicode("Tu eres " + u.getDescripcion());
	
		mandarmensaje(chat_id, mensaje, replyid);
	
		switch (u.getId()) {
	
		case 172732847: // alv
			mandarGif(chat_id, "CgADBAADYgMAAlgaZAc4DTQMurdKnQI", replyid);
			break;
	
		}
		;
	}


	private void quienEsAlias(Update update) {
	
		StringTokenizer tokens = new StringTokenizer(update.getMessage().getText(), " ");
	
		while (tokens.hasMoreTokens()) {
			String str = tokens.nextToken();
			if (str.startsWith("@")) {
				Usuario u = weje.buscarUsuario(str.substring(1));
				String mensaje = ".... nadie";
	
				mensaje = EmojiParser.parseToUnicode("Es " + u.getDescripcion());
	
				mandarmensaje(update.getMessage().getChatId(), mensaje, 0);
	
				switch (u.getId()) {
	
				case 172732847: // alv
					mandarGif(update.getMessage().getChatId(), "CgADBAADYgMAAlgaZAc4DTQMurdKnQI", 0);
	
					break;
	
				}
				;
			}
		}
	
	}


	private void quienEsReply(long user_id, long chat_id, long replyid, String alias) {
		System.out.println(user_id + alias);
		Usuario u = weje.buscarUsuario(alias);
		String mensaje = "";
	
		mensaje = EmojiParser.parseToUnicode("Es " + u.getDescripcion());
	
		mandarmensaje(chat_id, mensaje, replyid);
	
		switch (u.getId()) {
	
		case 172732847: // alv
			mandarGif(chat_id, "CgADBAADYgMAAlgaZAc4DTQMurdKnQI", replyid);
	
			break;
	
		}
		;
	
	}


	private boolean insultos(String mensaje, long chat_id, long replyid) {
		List<String> frases = new ArrayList<String>();
		frases.add("Amuermao");
		frases.add("Horchataman");
		frases.add("Llegas a ser más tonto y no naces");
		frases.add("Eres más feo que pegar a un padre con un calcetín sudao");
		frases.add("Deberías poner a trabajar la única neurona que te queda");
		frases.add("¿Tu eres tonto o barres playas?");
		frases.add("No eres más tonto porque no te entrenas.");
		frases.add("Cómprate un bosque y piérdete");
		frases.add("Si los cerdos volaran tú no tocabas el suelo");
		frases.add("Las fotos en las que sales tienen miedo a revelarse");
		frases.add("No eres feo, tu belleza es rara.");
		frases.add("¿Qué llevas en la mochila, Quasimodo?");
		frases.add("¿Pero tú de qué vas, piltrafiya?");
		frases.add("Eres más simple que el mecanismo de un chupete");
		frases.add("Llego a ser tú y me suicido");
		frases.add("Tesoro, ¿por qué no buscas alguien que te entierre?");
		frases.add("-¿Hola, tienes Instagram? \n+Sí \n- Pues bórratelo.");
		frases.add("Cara Anchoa");
		frases.add("Merluzo");
		frases.add("Comecésped");

		if (mensaje.contains("hijo de puta") || mensaje.contains("mierdaseca") || mensaje.contains("abrazafarolas")
				|| mensaje.contains("cabron") || mensaje.contains("cabrón") || mensaje.contains("lameculos")
				|| mensaje.contains("pagafantas") || mensaje.contains("tolai") || mensaje.contains("perroflauta")
				|| mensaje.contains("parguela") || mensaje.contains("cenutrio") || mensaje.contains("tocapelotas")
				|| mensaje.contains("pichabrava") || mensaje.contains("panoli") || mensaje.contains("merluzo")
				|| mensaje.contains("lerdo") || mensaje.contains("capullo") || mensaje.contains("mindundi")
				|| mensaje.contains("zopenco") || mensaje.contains("gilipolla") || mensaje.contains("idiota")
				|| mensaje.contains("imbécil") || mensaje.contains("imbecil") || mensaje.contains("soplapollas")
				|| mensaje.contains("retrasado") || mensaje.contains("subnormal") || mensaje.contains("tonto")
				|| mensaje.contains("malpario") || mensaje.contains("amuermao") || mensaje.contains("mamon")
				|| mensaje.contains("mamón") || mensaje.contains("zorra") || mensaje.contains("asqueroso")
				|| mensaje.contains("sinvergüenza") || mensaje.contains("payaso") || mensaje.contains("lelo")) {
			int random = (int) (Math.random() * frases.size());
			mandarmensaje(chat_id, frases.get(random), replyid);
			return true;
		}

		return false;
	}

	private void derrocarGobierno(long chat_id, long replyid) {
		if (idExpulsor == 173507959) { // Karaka
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

			int random = (int) (Math.random() * frases.size());
			mandarmensaje(chat_id, frases.get(random), replyid);

		} else if (idExpulsor == 39226004) { // Jairo
			List<String> frases = new ArrayList<String>();
			frases.add(EmojiParser.parseToUnicode("En serio?? :persevere:"));
			frases.add("Vaya creador...");
			frases.add("Podríamos crear otro grupo sin este tio...");
			frases.add("Este tio es otro dictador en la sombra...");
			frases.add("lalalalalala");
			frases.add("Seguro que ahora me va a expulsar... es un chulo. ");
			frases.add("Este tio se guarda los permisos de admin como si fueran oro...");
			frases.add("Crear un grupo, abusar de tu poder infinito...");
			frases.add("Prefiero al GLK antes que a ti, con eso te digo todo");
			frases.add("Santa paciencia");

			int random = (int) (Math.random() * frases.size());
			mandarmensaje(chat_id, frases.get(random), replyid);
		}

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

		int random = (int) (Math.random() * frases.size());
		mandarmensaje(chat_id, frases.get(random), replyid);

	}

	private void RuPaul(long chat_id, long replyid) {
		List<String> gifs = new ArrayList<String>();
		gifs.add("CgADBAADYgMAAlgaZAc4DTQMurdKnQI");
		gifs.add("CgADBAADdiMAAhccZAceAxVA7BlIHwI");
		gifs.add("CgADBAADAZcAAvAdZAdsJB75NqB_igI");
		gifs.add("CgADBAADMAMAAjweZAcBc0A1mhDcKgI");
		gifs.add("CgADBAADb58AAlIYZAdZVUCALZp9qAI");
		gifs.add("CgADBAADzL8AAvgZZAd_CUdTqsQeMgI");
		gifs.add("CgADBAADpaYAAhYdZAefrG8S-N8KYwI");

		int random = (int) (Math.random() * gifs.size());
		mandarGif(chat_id, gifs.get(random), replyid);

	}

	private void regimen(long chat_id, long replyid) {
		List<String> frases = new ArrayList<String>();
		frases.add("Como la R.D.A de Only Farm encabezada por el Gran Líder Karakatuchi no hay nada.");
		frases.add("Para gobierno bueno el del Gran Líder Karakatuchi, fua que bien lo hace el cabrón.");
		frases.add(
				"He oido por ahí que dentro de poco en la R.D.A habrá elecciones, pero dificil veo yo que alguien HUMANO sea capaz de derrotar al peaso de líder que tienen. Es un fiera el tío");
		frases.add(
				"Pero seguro que ese del que hablas no tiene una constitución tan buena como la que creó nuestro Gran Líder Karakatuchi para la R.D.A de Only Farm.");

		int random = (int) (Math.random() * frases.size());
		mandarmensaje(chat_id, frases.get(random), replyid);

	}

	private void buenosdias(long chat_id, long replyid, long userid, String alias) {
		Date horaActual = new Date();
		DateFormat dateF = new SimpleDateFormat("HH:mm");
		try {
			bdias = dateF.parse(bdias.getHours() + ":" + bdias.getMinutes());
			horaActual = dateF.parse(horaActual.getHours() + ":" + horaActual.getMinutes());
			System.out.println(horaActual.getTime() - bdias.getTime());
			// if (horaActual.getTime() -bdias.getTime() >= 10*60*1000) {

			Date horaIni = dateF.parse("6:00");
			Date horaFin = dateF.parse("12:00");

			if (horaActual.before(horaFin) && horaActual.after(horaIni)) {
				if (userid == 173507959) { // GLK
					mandarSticker(chat_id, "CAADBAADjgAD70_CCUCbuRnXNN7uAg");
				} else
					mandarmensaje(chat_id, "¡¡Buenos días @" + alias + "!!, ¿Has dormido bien? :)", replyid);
				// mandarGif(chat_id,"CgADAQADsgADEH4SDZPeelLNvJZCAg",replyid);
			} else
				mandarmensaje(chat_id, "En serio? buenos días a estas horas? ... revísatelo...", replyid);

			bdias = horaActual;
			// }
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}

	private void buenasnoches(long chat_id, String nombre, long replyid, long id) {

		try {
			DateFormat dateF = new SimpleDateFormat("HH:mm");
			Date horaActual = new Date();
			bnoches = dateF.parse(bnoches.getHours() + ":" + bnoches.getMinutes());
			horaActual = dateF.parse(horaActual.getHours() + ":" + horaActual.getMinutes());
			Date horaIni1 = dateF.parse("21:00");
			Date horaFin1 = dateF.parse("23:59");
			Date horaIni2 = dateF.parse("00:00");
			Date horaFin2 = dateF.parse("6:00");
			Date hora1 = dateF.parse("12:00");

			System.out.println(horaActual.getTime() - bnoches.getTime());

			// if(horaActual.getTime() -bnoches.getTime() >= 10*60*1000) {

			if ((horaActual.before(horaFin1) && horaActual.after(horaIni1))
					|| (horaActual.before(horaFin2) && horaActual.after(horaIni2))) {
				if (id != 72263992) {
					mandarmensaje(chat_id, "¡Buenas noches! Que descanses " + nombre + "!", replyid);
				} else {
					List<String> frases = new ArrayList<String>();
					frases.add("Ya pensaba que no me ibas a decir nada " + nombre + "... Que descanses!!! :)");
					frases.add(EmojiParser.parseToUnicode("Espero que sueñes conmigo! :smirk:"));
					frases.add(EmojiParser
							.parseToUnicode("Ojalá pudiera ser human@ para irme a dormir contigo :kissing:"));
					frases.add(EmojiParser
							.parseToUnicode("Como me gusta darte las buenas noches todos los días, que descanses!"));
					frases.add(EmojiParser.parseToUnicode(
							"Las noches son demasiado largas cuando estás lejos de mi... Buenas noches... :cry:"));

					int random = (int) (Math.random() * frases.size());
					mandarmensaje(chat_id, frases.get(random), 0);

					mandarGif(chat_id, "CgADBAADXAMAAjUZZAfk7Q1aA0kstQI", replyid);

				}

			} else if (horaActual.before(horaIni1) && horaActual.after(hora1)) {
				mandarmensaje(chat_id, "¿No es un poco pronto para irse a dormir?", replyid);
			} else if (horaActual.after(horaFin2) && (horaActual.before(hora1))) {
				mandarmensaje(chat_id, "Pero si ya es hora estar levantado, ¡¡A trabajar!!", replyid);

			}

			bnoches = horaActual;
			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}

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

	private void mandarGif(long chat_id, String document, long replyid) {

		SendDocument s = new SendDocument();
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

		SendPhoto ph = new SendPhoto();
		ph.setPhoto(document);
		ph.setChatId(chat_id);
		try {
			super.sendPhoto(ph);
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

	private boolean comprobarComando(long chat_id, String mensaje, long replyid) {
		boolean ejecutado = false;
		ArrayList<Comando> comandos = weje.ObtenerComandos();
	
		for (int i = 0; i < comandos.size(); i++) {
			Comando c = comandos.get(i);
	
			if (mensaje.contains(c.getComando().toLowerCase())) {
				ejecutado = true;
				switch (c.getTipo()) {
				case "m":
					mandarmensaje(chat_id, c.getRespuesta(), replyid);
					break;
				case "d":
					mandarGif(chat_id, c.getRespuesta(), replyid);
					break;
				case "s":
					mandarSticker(chat_id, c.getRespuesta());
					break;
				case "f":
					mandarFoto(chat_id, c.getRespuesta(), replyid);
					break;
				}
			}
		}
		return ejecutado;
	}


	private void crearBotones(long chat_id, long message_id, String answer, String texto, String callback) {
	
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
		List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
	
		rowInline.add(new InlineKeyboardButton().setText(texto).setCallbackData(callback));
		rowsInline.add(rowInline);
	
		EditMessageText new_message = new EditMessageText().setChatId(chat_id).setMessageId(toIntExact(message_id))
				.setText(answer);
	
		markupInline.setKeyboard(rowsInline);
	
		new_message.setReplyMarkup(markupInline);
		try {
			execute(new_message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	private void crearBotonesPrimero(String mensaje, long chat_id, String texto, String callback) {
	
		SendMessage message = new SendMessage() // Create a message object object
				.setChatId(chat_id).setText(mensaje);
	
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
		List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
	
		rowInline.add(new InlineKeyboardButton().setText(texto).setCallbackData(callback));
	
		// Set the keyboard to the markup
		rowsInline.add(rowInline);
		// Add it to the message
	
		markupInline.setKeyboard(rowsInline);
		message.setReplyMarkup(markupInline);
	
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	public String getBotUsername() {
		return "OnlyFarm_bot";
	}

	@Override
	public String getBotToken() {
		return "494431475:AAFqQAFY_d56ljbL8OZEI4MK-j4uV8Ncp_0";
	}

	private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out
				.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
		System.out.println("Bot answer: \n Text - " + bot_answer);
		
		System.out.println("\n ----------------------------");
	}

	private void help(long chat_id, long replyid) {
		String mensaje = "Funciones de moderación\r\n"
				+ "(algunas requieren de permisos de admin en el grupo para funcionar):\r\n"
				+ "-/warn a usuario respondiendo a un mensaje del usuario (solo para admin)\r\n"
				+ "-Expulsión automática de un usuario con 3 /warn (solo para admin)\r\n"
				+ "-Reseteo de los warns actuales de un usuario con /unwarn respondiendo a un mensaje del usuario (solo para admin)\r\n"
				+ "-Expulsión directa de un usuario con /kick respondiendo a un mensaje (solo para admin)\r\n"
				+ "-Silenciar a un usuario con /mute respondiendo a un mensaje del usuario (solo para admin)\r\n"
				+ "-Desilenciar a un usuario con /unmute respondiendo a un mensaje del usuario (solo para admin)\r\n"
				+ "-Denunciar un mensaje ante el GLK con /report, respondiendo a dicho mensaje.\r\n"
				+ "-Estadísticas de warns actuales y totales, así como conteo de mensajes, con !estadisticas.\r\n"
				+ "-Función de anclaje respondiendo 'ancla esto' al mensaje que quieras anclar (requiere admin en el bot)\r\n"
				+ "\r\n" + "Funciones de la R.D.A:\r\n" + "-!lider para enseñar el cartel de nuestro amado GLK.\r\n"
				+ "-!bandera para mostrar la bandera oficial de la R.D.A.\r\n"
				+ "-!constitución para obtener la constitución oficial de la R.D.A.\r\n"
				+ "-!sello para obtener el sello oficial de la R.D.A.\r\n" + "\r\n" + "Funciones de interés:\r\n"
				+ "-/roll permite lanzar hasta 20 dados de un set de rol corriente.\r\n"
				+ "-/time permite activar una cuenta atrás en xh ym zs con asunto específico.\r\n"
				+ "-@todos para mencionar rápidamente a todos los integrantes del grupo.\r\n"
				+ "-@todoscc para mencionar rápidamente a todos los de cc.\r\n"
				+ "-Se puede comprobar rápidamente el estado del bot diciendo 'Server on' o mencionando al bot @OnlyFarm_Bot\r\n"
				+ "-\"ancla esto\" permite anclar el mensaje al que respondas\r\n" + "\r\n" + "Otros:\r\n"
				+ "-El bot responde a los buenos días y a las buenas noches.\r\n"
				+ "-El bot le da la bienvenida a todo aquel que entra en el grupo, a la manera que dicta la R.D.A.\r\n"
				+ "-También recuerda quién es quién. Puedes preguntarle Quién soy, Quién es @alguien, o Quién es alguien que hayas respondido su mensaje.\r\n"
				+ "-Al bot no le cae bien Toni, y te lo hará saber cada vez que se lo menciones.\r\n"
				+ "-Le gusta un poco Rupaul, y si le hablas de él, te pasará un gif de la emoción.\r\n"
				+ "-Al bot no le gusta mucho las traidiciones, y como se las nombres, te enviará el gif oficial de la tradición.\r\n"
				+ "-El bot también ayuda cuando quieres convencer a alguien, y si dices Trust me, te ayudará a argumentar.\r\n"
				+ "-El bot permite dejar bien claro quién se pide primero a alguien. Así que, si dices me lo/la pido, te sacará una lista para dejarlo bien claro\r\n"
				+ "-Modo antigobierno corrupto.";

		mandarmensaje(chat_id, mensaje, replyid);

	}

}
