/*
Нахождение всех возможных вариантов
*/

class transposition {
	public static void main( String[] args ) {
		char[] symbols = { 'A', 'B', 'C' };
		arrayOut( getOptionsNoRepeat( symbols, 3 ));
	}
	
	//вывод массива
	public static void arrayOut( char[][] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			for ( int i2 = 0; i2 < array[0].length; i2++ ) {
				System.out.print( array[i][i2] + " " );
			}
			System.out.println();
		}
	}
	
	//получить все возможные варианты без повторений
	public static char[][] getOptionsNoRepeat( char[] array ) {
		return getOptionsNoRepeat( array, array.length );
	}
	
	//получить все возможные варианты по k без повторений
	public static char[][] getOptionsNoRepeat( char[] array, int k ) {
		int numOptions = (int)Math.pow( array.length, k );
		int numOptionsNoRepeat = getFactorial( array.length ) / getFactorial( array.length - k );
		int[] indexOptions = new int[k];
		char[][] options = new char[numOptionsNoRepeat][k];
		
		int lineNum = 0;
		for ( int i = 0; i < numOptions; i++ ) {
			for ( int i2 = 0; i2 < k; i2++ ) {
				if ( indexOptions[i2] < array.length - 1 ) {
					indexOptions[i2] += 1;
					break;
				} else {
					indexOptions[i2] = 0;
				}
			}
			
			if ( identicalValues( indexOptions ) == true ) {
				continue;
			}
			
			for ( int i2 = 0; i2 < indexOptions.length; i2++ ) {
				options[lineNum][i2] = array[indexOptions[i2]];
			}
			lineNum += 1;
		}
		return options;
	}
	
	//проверка на одинаковые значения
	public static boolean identicalValues( int[] indexOptions ) {
		for ( int i = 0; i < indexOptions.length; i++ ) {
			for ( int i2 = 0; i2 < indexOptions.length; i2++ ) {
				if (( indexOptions[i] == indexOptions[i2] ) & ( i != i2 )) {
					return true;
				}
			}
		}
		return false;
	}
	
	//получить факториал
	public static int getFactorial( int n ) {
		int factorial = 1;
		for ( int i = 1; i <= n; i++ ) {
			factorial *= i;
		}
		return factorial;
	}
	
	//получить все возможные варианты
	public static char[][] getOptions( char[] array ) {
		return getOptions( array, array.length );
	}
	
	//получить все возможные варианты по k
	public static char[][] getOptions( char[] array, int k ) {
		int numOptions = (int)Math.pow( array.length, k );
		int[] indexOptions = new int[k];
		char[][] options = new char[numOptions][k];
		
		for ( int i = 0; i < numOptions; i++ ) {
			for ( int i2 = 0; i2 < k; i2++ ) {
				if ( indexOptions[i2] < array.length - 1 ) {
					indexOptions[i2] += 1;
					break;
				} else {
					indexOptions[i2] = 0;
				}
			}
			
			for ( int i2 = 0; i2 < indexOptions.length; i2++ ) {
				options[i][i2] = array[indexOptions[i2]];
			}
		}
		return options;
	}
}