/*
Нахождение всех возможных вариантов
*/

class transposition {
	public static void main( String[] args ) {
		char[] symbols = { 'A', 'B', 'C' };
		arrayOut( getOptions( symbols ));
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
	
	//получить все возможные варианты по k
	public static char[][] getOptions( char[] array, int k ) {
		int numOptions = (int)Math.pow( array.length, k );
		char[][] options = getOptions( array );
		char[][] optionsK = new char[numOptions][k];
		
		int index = 0;
		for ( int i = 0; i < options.length; i++ ) {
			char[] option = new char[k];
			for ( int i2 = 0; i2 < k; i2++ ) {
				option[i2] = options[i][i2];
			}
			
			if ( optionExists( optionsK, option ) == false ) {
				for ( int i2 = 0; i2 < k; i2++ ) {
					optionsK[index][i2] = options[i][i2];
				}
				index += 1;
			}
		}
		
		return optionsK;
	}
	
	//проверка существования варианта
	public static boolean optionExists( char[][] options, char[] option ) {
		String inOption = "";
		for ( int i = 0; i < option.length; i++ ) {
			inOption += option[i];
		}
		
		for ( int i = 0; i < options.length; i++ ) {
			String inOptions = "";
			for ( int i2 = 0; i2 < option.length; i2++ ) {
				inOptions += options[i][i2];
			}
			
			if ( inOptions.equals( inOption ) == true ) {
				return true;
			}
		}
		return false;
	}
	
	//получить все возможные варианты
	public static char[][] getOptions( char[] array ) {
		int numOptions = (int)Math.pow( array.length, array.length );
		int num = numOptions / array.length;
		char[][] options = new char[numOptions][array.length];
		
		for ( int i = 0; i < array.length; i++ ) {
			for ( int i2 = 0; i2 < numOptions; i2++ ) {
				int index = (int)Math.floor( i2 / num );
				if ( index >= array.length ) {
					index -= (int)Math.floor( index / array.length ) * array.length;
				}
				options[i2][i] = array[index];
			}
			num /= array.length;
		}
		
		return options;
	}
}