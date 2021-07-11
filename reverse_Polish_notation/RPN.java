import java.util.ArrayList;
import java.util.Stack;

class RPN {
	public static void main( String[] args ) {
		String expression = "15 * -(3 - 4)";
		expression = "( 10 + sin( 10.5 + 80.5 )) % 3";
		System.out.println( calcRPN( getRPN( expression )));
	}
	
	/*-----------------------------
	методы перевода выражения в ОПН
	-----------------------------*/
	//расчет ОПН
	public static double calcRPN( String[] array ) {
		Stack<Double> answer = new Stack<Double>();
		
		for ( int i = 0; i < array.length; i++ ) {
			if ( stringNumber( array[i] ) == true ) {
				answer.push( Double.parseDouble( array[i] ));
			}
			
			if ( array[i].equals( "sqrt" ) == true ) {
				answer.push( Math.sqrt( answer.pop()));
			}
			
			if ( array[i].equals( "sin" ) == true ) {
				answer.push( Math.sin( answer.pop()));
			}
			
			if ( array[i].equals( "cos" ) == true ) {
				answer.push( Math.cos( answer.pop()));
			}
			
			if ( array[i].equals( "tg" ) == true ) {
				answer.push( Math.tan( answer.pop()));
			}
			
			if ( array[i].equals( "ctg" ) == true ) {
				answer.push( ctan( answer.pop()));
			}
			
			if ( array[i].equals( "u" ) == true ) {
				answer.push( - answer.pop());
			}
			
			if ( stringOperation( array[i] ) == true ) {
				double numberB = answer.pop();
				double numberA = answer.pop();
				
				if ( array[i].equals( "-" ) == true ) {
					answer.push( numberA - numberB );
				}
				
				if ( array[i].equals( "+" ) == true ) {
					answer.push( numberA + numberB );
				}
				
				if ( array[i].equals( "*" ) == true ) {
					answer.push( numberA * numberB );
				}
				
				if ( array[i].equals( "/" ) == true ) {
					answer.push( numberA / numberB );
				}
				
				if ( array[i].equals( "^" ) == true ) {
					answer.push( Math.pow( numberA, numberB ));
				}
				
				if ( array[i].equals( "%" ) == true ) {
					answer.push( numberA % numberB );
				}
			}
		}
		
		return answer.pop();
	}
	
	//перевод в ОПН
	public static String[] getRPN( String value ) {
		String[] values = getDivision( value );
		
		ArrayList<String> result = new ArrayList<String>();
		Stack<String> operations = new Stack<String>();
		
		for ( int i = 0; i < values.length; i++ ) {
			if ( stringNumber( values[i] ) == true ) {
				result.add( values[i] );
			}
			
			if ( stringFunction( values[i] ) == true ) {
				operations.push( values[i] );
			}
			
			if ( values[i].equals( "(" ) == true ) {
				operations.push( values[i] );
			}
			
			if ( values[i].equals( ")" ) == true ) {
				while ( operations.peek().equals( "(" ) == false ) {
					result.add( operations.pop());
				}
				operations.pop();

				if ( operations.empty() == false ) {
					if ( stringFunction( operations.peek()) == true ) {
						result.add( operations.pop());
					}
				}
			}
			
			if ( stringOperation( values[i] ) == true ) {
				if ( stringUnary( values, i ) == true ) {
					operations.push( "u" );
				} else {
					while ( continueChecking( operations, values[i] ) == true ) {
						result.add( operations.pop());
					}
					operations.push( values[i] );
				}
			}
		}
		
		while ( operations.empty() == false ) {
			result.add( operations.pop());
		}
		
		String[] newValues = new String[result.size()];
		for ( int i = 0; i < newValues.length; i++ ) {
			newValues[i] = result.get( i );
		}
		
		return newValues;
	}
	
	//получить котангенс
	private static double ctan( double value ) {
		return Math.cos( value ) / Math.sin( value );
	}
	
	//проверяем является ли строка унарным минусом
	private static boolean stringUnary( String[] array, int index ) {
		char[] symbols = { '(', '+', '-', '*', '/' }; 
		
		if ( array[index].equals( "-" ) == true ) {
			if ( index == 0 ) {
				return true;
			}
			
			if ( index - 1 >= 0 ) {
				for ( int i = 0; i < symbols.length; i++ ) {
					if ( symbols[i] == array[index - 1].charAt( 0 )) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//вывод массива
	private static void outArray( String[] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			System.out.print( array[i] + " " );
		}
	}
	
	//проверка что приоритет текущего знака меньше или равен 
	//последнему из стака операторов и что последний не пустой
	private static boolean continueChecking( Stack<String> stack, String value ) {
		if ( stack.empty() == false ) {
			int priorityA = getPriority( value.charAt( 0 ));
			int priorityB = getPriority( stack.peek().charAt( 0 ));
			
			if ( priorityB >= priorityA ) {
				return true;
			}
		}
		
		return false;
	}
	
	//проверяем является ли строка функцией
	private static boolean stringFunction( String value ) {
		String[] functions = { "sqrt", "sin", "cos", "tg", "ctg" };
		
		for ( int i = 0; i < functions.length; i++ ) {
			if ( value.equals( functions[i] ) == true ) {
				return true;
			}
		}
		
		return false;
	}
	
	//проверяем является ли строка оператором
	private static boolean stringOperation( String value ) {
		char[] operations = { '-', '+', '*', '/', '^', '%' };
		
		for ( int i = 0; i < operations.length; i++ ) {
			if ( value.charAt( 0 ) == operations[i] ) {
				return true;
			}
		}
		
		return false;
	}
	
	//проверяем является ли строка числом
	private static boolean stringNumber( String value ) {
		for ( int i = 0; i < value.length(); i++ ) {
			char symbolA = value.charAt( i );
			
			boolean number = false;
			for ( int i2 = 0; i2 < 10; i2++ ) {
				char symbolB = Integer.toString( i2 ).charAt( 0 );
				
				if ( symbolA == symbolB ) {
					number = true;
				}
			}
			
			if ( symbolA == '.' ) {
				number = true;
			}
			
			if ( number == false ) {
				return false;
			}
		}
		
		return true;
	}
	
	//получить приоритет операции
	private static int getPriority( char value ) {
		char[][] operations = {
			{ '-', '+', '*', '/', '^', '%' }, /*значения*/
			{ '1', '1', '2', '2', '3', '3' } /*приоритет*/
		};
		
		for ( int i = 0; i < operations[0].length; i++ ) {
			if ( operations[0][i] == value ) {
				return Integer.parseInt( String.valueOf( operations[1][i] ));
			}
		}
		
		return 0;
	}
	
	/*-----------------------------------
	методы преобразования строки в массив
	-----------------------------------*/
	//разбиваем числа и операции на строковый массив
	private static String[] getDivision( String functionValue ) {
		functionValue = delSpace( functionValue );
		
		ArrayList<String> lines = new ArrayList<String>();
		String number = "";
		String function = "";
		
		for ( int i = 0; i < functionValue.length(); i++ ) {
			char symbol = functionValue.charAt( i );
			
			if ( isThisNumber( symbol ) == true ) {
				number += symbol;
			}
			
			if ( isThisLetter( symbol ) == true ) {
				function += symbol;
			}
			
			if ( isThisOperation( symbol ) == true ) {
				if ( number.equals( "" ) == false ) {
					lines.add( number );
					number = "";
				}
				if ( function.equals( "" ) == false ) {
					lines.add( function );
					function = "";
				}
				lines.add( Character.toString( symbol ));
			}
			
			if ( i == functionValue.length() - 1 ) {
				if ( number.equals( "" ) == false ) {
					lines.add( number );
				}
				
				if ( function.equals( "" ) == false ) {
					lines.add( function );
				}
			}
		}
		
		String[] newLines = new String[lines.size()];
		for ( int i = 0; i < lines.size(); i++ ) {
			newLines[i] = lines.get( i );
		}
		
		return newLines;
	}
	
	//проверяем является ли символ буквой
	private static boolean isThisLetter( char value ) {
		for ( int i = 65; i <= 90; i++ ) {
			if ((char)i == value ) {
				return true;
			}
		}
		
		for ( int i = 97; i <= 122; i++ ) {
			if ((char)i == value ) {
				return true;
			}
		}
		
		return false;
	}
	
	//проверяем является ли символ операцией
	private static boolean isThisOperation( char value ) {
		char[] operations = { '(', ')', '-', '+', '*', '/', '^', '%' };
		
		for ( int i = 0; i < operations.length; i++ ) {
			if ( operations[i] == value ) {
				return true;
			}
		}
		
		return false;
	}
	
	//проверяем является ли символ цифрой или точкой
	private static boolean isThisNumber( char value ) {
		for ( int i = 0; i < 10; i++ ) {
			char number = Integer.toString( i ).charAt( 0 );
			if ( number == value ) {
				return true;
			}
		}
		
		if ( value == '.' ) {
			return true;
		}
		
		return false;
	}
	
	//удаление пробелов из текста
	private static String delSpace( String value ) {
		return value.replaceAll( " ", "" );
	}
}