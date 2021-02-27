/*
импорт классов из:
https://github.com/rubenlagus/TelegramBots/releases/tag/v3.6 - TelegramBots(Api version 3.6)
telegrambots-3.6-jar-with-dependencies.jar
*/
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class telegabot extends TelegramLongPollingBot {
	public static void main( String[] args ) {
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot( new telegabot());
		} catch ( TelegramApiRequestException e ) {
			e.printStackTrace();
		}
	}
	
	public void onUpdateReceived( Update update ) {
		Message message = update.getMessage();
		if (( message != null ) & ( message.hasText())) {
			String command = message.getText();
			if ( command.equals( "/table" ) == true ) {
				String table = urfutable.getURFUTable();
				if ( table != null ) {
					sendMsg( message, table );
				} else {
					sendMsg( message, "error" );
				}
			}
			if ( command.equals( "/help" ) == true ) {
				sendMsg( message, "/table - out URFU table\n" );
			}
		}
	}
	
	public void sendMsg( Message message, String text ) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown( true );
		sendMessage.setChatId( message.getChatId().toString());
		sendMessage.setReplyToMessageId( message.getMessageId());
		sendMessage.setText( text );
		try {
			sendMessage( sendMessage );
		} catch ( TelegramApiException e ) {
			e.printStackTrace();
		}
	}
	
	public String getBotUsername() {
		return "tableBot";
	}
	
	public String getBotToken() {
		return "1583259679:AAFZp6uZsOMZsh5LOioX5xVIVRjBYQhre9M";
	}
}