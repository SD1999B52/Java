/*
получим расписание URFU (текст возвращается в UTF-8 кодировке)
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class urfutable {
	final static String[] arrayMonth = new String[] {
		"января", "февраля", "марта", "апреля", "мая", "июня", "июля",
		"августа", "сентября", "октября", "ноября", "декабря"
	};
	
	final static String[] arrayDay = new String[] {
		"Воскресенье", "Понедельник", "Вторник",
		"Среда", "Четверг", "Пятница", "Суббота"
	};
	
	public static void main( String[] args ) {
		try {
			//получим код страницы
			URL address = new URL( "https://urfu.ru/api/schedule/groups/lessons/984802/" + getDate() + "/" );
			HttpURLConnection connection = ( HttpURLConnection )address.openConnection();
			connection.setRequestProperty( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11" );
			ArrayList<String> sheet = getSheet( connection );
			
			ArrayList<String> data = getData( sheet );
			String[][] table = getTable( data );
			outTable( table );
			
			connection.disconnect();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	//вывод массива String[][]
	public static void outTable( String[][] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			boolean current = false;
			if ( array[i][0] != null ) {
				if ( i - 1 >= 0 ) {
					if ( array[i][0].equals( array[i - 1][0] ) == false ) {
						System.out.println( "----------------------------------------" );
						System.out.println( array[i][0] + "(" + getNameDay( array[i][0] ) + ")" );
						System.out.println();
					}
				} else {
					System.out.println( array[i][0] + "(" + getNameDay( array[i][0] ) + ")" );
					System.out.println();
				}
				if ( array[i][0].equals( getDayMonth()) == true ) {
					current = true;
				}
			}
			if ( array[i][1] != null ) {
				String timeA = array[i][1].substring( 0, 5 );
				String timeB = array[i][1].substring( 8, 8 + 5 );
				if (( timeInclud( timeA, timeB ) == true ) & ( current == true )) {
					System.out.println( ">" + array[i][1] + "<" );
				} else {
					System.out.println( array[i][1] );
				}
			}
			for ( int i2 = 2; i2 < array[0].length; i2++ ) {
				if ( array[i][i2] != null ) {
					System.out.println( " | " + array[i][i2] );
				}
			}
			System.out.println();
		}
	}
	
	//преобразуем ArrayList в String[][] массив
	public static String[][] getTable( ArrayList<String> list ) {
		String[][] table = new String[list.size()][getColNum( list )];
		for ( int i = 0; i < list.size(); i++ ) {
			String line = list.get( i );
			String text = "";
			int col = 0;
			for ( int i2 = 0; i2 < line.length(); i2++ ) {
				if ( line.charAt( i2 ) != '|' ) {
					text += line.charAt( i2 );
				} else {
					table[i][col] = text;
					col += 1;
					text = "";
				}
			}
		}
		return table;
	}
	
	//получим количество столбцов для массива
	public static int getColNum( ArrayList<String> list ) {
		int max = 0;
		for ( int i = 0; i < list.size(); i++ ) {
			String line = list.get( i );
			int lineMax = 0;
			for ( int i2 = 0; i2 < line.length(); i2++ ) {
				if ( line.charAt( i2 ) == '|' ) {
					lineMax += 1;
				}
			}
			if ( lineMax > max ) {
				max = lineMax;
			}
		}
		return max + 1;
	}
	
	//получим новый ArrayList с данными
	public static ArrayList<String> getData( ArrayList<String> sheet ) {
		ArrayList<String> newtable = new ArrayList<String>();
		
		//получим строки таблицы
		ArrayList<String> tableLines = getValue( sheet, "<tr class=", "</tr>" );
		
		//удаляем лишние пробелы
		for ( int i = 0; i < tableLines.size(); i++ ) {
			String ln = tableLines.get( i );
			tableLines.set( i, delSpace( ln ));
		}
		
		String saveday = "";
		for ( int i = 0; i < tableLines.size(); i++ ) {
			ArrayList<String> day = getValueString( tableLines.get( i ), "<b>", "</b>" );
			if ( day != null ) {
				saveday = day.get( 0 );
			}
			
			ArrayList<String> time = getValueString( tableLines.get( i ), "<td class=\"shedule-weekday-time\">", "</td>" );
			ArrayList<String> subjects = getValueString( tableLines.get( i ), "<dd>", "</dd>" );
			ArrayList<String> dt = getValueString( tableLines.get( i ), "<dt>", "</dt>" );
			
			if (( subjects != null ) & ( time != null ) & ( dt != null )) {
				ArrayList<String> cabinet = getValue( dt, "<span class=\"cabinet\">", "</span>" );
				ArrayList<String> teacher = getValue( dt, "<span class=\"teacher\">", "</span>" );
				
				String line = saveday + "|" + time.get( 0 ) + "|" + delNumSubject( subjects.get( 0 ));
				
				if (( cabinet != null ) & ( teacher != null )) {
					if ( thisIsALink( cabinet.get( 0 )) == true ) {
						ArrayList<String> incabinet = getValue( dt, "target=\"_blank\">", "</a>" );
						if ( incabinet != null ) {
							line += "|" + incabinet.get( 0 );
						}
					} else {
						line += "|" + delSpaceAddress( cabinet.get( 0 ));
					}
					
					for ( int i2 = 0; i2 < teacher.size(); i2++ ) {
						line += "|" + teacher.get( i2 );
					}
				}
				
				newtable.add( line + "|" );
			}
		}
		return newtable;
	}
	
	//удаляем лишний пробел перед адресом
	public static String delSpaceAddress( String line ) {
		return line.substring( 1, line.length());
	}
	
	//удаляем номер перед предметом
	public static String delNumSubject( String line ) {
		String newline = "";
		int space = 2;
		for ( int i = 0; i < line.length(); i++ ) {
			if ( space == 0 ) {
				newline += line.charAt( i );
			}
			if (( line.charAt( i ) == ' ' ) & ( space != 0 )) {
				space -= 1;
			}
		}
		return newline;
	}
	
	//проверка ссылки
	public static boolean thisIsALink( String line ) {
		String link = "href";
		for ( int i = 0; i < line.length(); i++ ) {
			if ( i + link.length() <= line.length()) {
				String teg = line.substring( i, i + link.length());
				if ( teg.equals( link ) == true ) {
					return true;
				}
			}
		}
		return false;
	}
	
	//удаляем пробелы
	public static String delSpace( String line ) {
		String text = "";
		for ( int i = 0; i < line.length(); i++ ) {
			if ( i + 1 < line.length()) {
				if (( line.charAt( i ) == ' ' ) & ( line.charAt( i + 1 ) != ' ' )) {
					text += ' ';
				}
			}
			if ( line.charAt( i ) != ' ' ) {
				text += line.charAt( i );
			}
		}
		return text;
	}
	
	//получим внутренности тега(ов) в строке
	public static ArrayList<String> getValueString( String line, String startTeg, String finalTeg ) {
		ArrayList<String> lineList = new ArrayList<String>();
		lineList.add( line );
		ArrayList<String> values = getValue( lineList, startTeg, finalTeg );
		if ( values.size() != 0 ) {
			return values;
		}
		return null;
	}
	
	//получим внутренности тега(ов)
	public static ArrayList<String> getValue( ArrayList<String> list, String startTeg, String finalTeg ) {
		ArrayList<String> fromTag = new ArrayList<String>();
		String text = "";
		boolean search = false;
		for ( int i = 0; i < list.size(); i++ ) {
			String line = list.get( i );
			for ( int i2 = 0; i2 < line.length(); i2++ ) {
				if (( i2 + startTeg.length() <= line.length()) & ( search == false )) {
					String teg = line.substring( i2, i2 + startTeg.length());
					if ( teg.equals( startTeg ) == true ) {
						i2 += startTeg.length();
						search = true;
					}
				}
				if (( i2 + finalTeg.length() <= line.length()) & ( search == true )) {
					String teg = line.substring( i2, i2 + finalTeg.length());
					if ( teg.equals( finalTeg ) == true ) {
						search = false;
						fromTag.add( text );
						text = "";
					}
				}
				if ( search == true ) {
					text += line.charAt( i2 );
				}
			}
		}
		return fromTag;
	}
	
	//получим html страницу
	public static ArrayList<String> getSheet( HttpURLConnection value ) {
		try {
			ArrayList<String> sheet = new ArrayList<String>();
			BufferedReader sheetbuffer = new BufferedReader( new InputStreamReader( value.getInputStream(), "utf-8" ));
			String line = sheetbuffer.readLine();
			while ( line != null ) {
				sheet.add( line );
				line = sheetbuffer.readLine();
			}
			sheetbuffer.close();
			return sheet;
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNameDay( String date ) {
		Calendar calendar = Calendar.getInstance();
		
		String dayStr = date.substring( 0, 2 );
		String monthStr = date.substring( 3, date.length());
		
		int year = calendar.get( Calendar.YEAR );
		
		int month = 0;
		for ( int i = 0; i < arrayMonth.length; i++ ) {
			String UTF8Month = getUTF8( arrayMonth[i] );
			if ( UTF8Month.equals( monthStr ) == true ) {
				month = i;
				break;
			}
		}
		
		int day = Integer.parseInt( dayStr );
		
		calendar.set( Calendar.DAY_OF_MONTH, day );
		calendar.set( Calendar.MONTH, month );
		calendar.set( Calendar.YEAR, year );
		
		int nameIdDay = calendar.get( Calendar.DAY_OF_WEEK ) - 1;
		
		return getUTF8( arrayDay[nameIdDay] );
	}
	
	//получить текущий день и месяц
	public static String getDayMonth() {
		String date = getDate();
		String day = date.substring( 6, 6 + 2 );
		String month = date.substring( 4, 4 + 2 );
		String monthName = arrayMonth[Integer.parseInt( month ) - 1];
		return day + " " + getUTF8( monthName );
	}
	
	//текст из windows-1251 в utf-8
	public static String getUTF8( String value ) {
		try {
			String utf8String = new String( value.getBytes( "windows-1251" ), "UTF-8" );
			return utf8String;
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	//проверка вхождения текущего времени в интервал
	public static boolean timeInclud( String timeA, String timeB ) {
		SimpleDateFormat timeFormat = new SimpleDateFormat( "HH:mm" );
		try {
			Date dateA = timeFormat.parse( timeA );
			Date dateB = timeFormat.parse( timeB );
			Date myTime = timeFormat.parse( getTime());
			if (( myTime.after( dateA ) == true ) & ( myTime.before( dateB ) == true )) {
				return true;
			}
			if (( myTime.equals( dateA ) == true ) | ( myTime.equals( dateB ) == true )) {
				return true;
			}
		} catch ( ParseException e ) {
			e.printStackTrace();
		}
		return false;
	}
	
	//получим текущую дату
	public static String getDate() {
		Calendar calendar = Calendar.getInstance();
		
		int day = calendar.get( Calendar.DAY_OF_MONTH );
		String dayStr = getNormalNum( day, 2 );
		
		int month = calendar.get( Calendar.MONTH ) + 1;
		String monthStr = getNormalNum( month, 2 );
		
		int yearStr = calendar.get( Calendar.YEAR );
		
		return yearStr + monthStr + dayStr;
	}
	
	//получим текущее время
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		
		int hour = calendar.get( Calendar.HOUR_OF_DAY );
		String hourStr = getNormalNum( hour, 2 );
		
		int minute = calendar.get( Calendar.MINUTE );
		String minuteStr = getNormalNum( minute, 2 );
		
		return hourStr + ":" + minuteStr;
	}
	
	//получим отрисовку числа в необходимом виде
	public static String getNormalNum( int value, int numberOfChar ) {
		String text = "";
		for ( int i = 1; i < numberOfChar; i++ ) {
			if ( value < Math.pow( 10, i )) {
				text += "0";
			}
		}
		return text + Integer.toString( value );
	}
}