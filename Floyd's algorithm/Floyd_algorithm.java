/*
Алгоритм Флойда
*/

class Floyd_algorithm {
	static int inf = Integer.MAX_VALUE;//infinity
	
	public static void main( String[] args ) {
		/*
		int[][] array = {
			{ 0, 8, 5 },
			{ 3, 0, inf },
			{ inf, 2, 0 }
		};
		*/
		
		int[][] array = {
			{ 0, 1, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf },
			{ inf, 0, 10, inf, inf, 5, inf, inf, inf, inf, 2, inf },
			{ inf, inf, 0, inf, inf, inf, 3, inf, inf, inf, inf, inf },
			{ inf, inf, 3, 0, inf, inf, 5, inf, inf, inf, inf, inf },
			{ inf, 5, inf, inf, 0, inf, inf, inf, inf, inf, inf, inf },
			{ inf, inf, inf, inf, 3, 0, 7, inf, inf, inf, inf, inf },
			{ inf, inf, inf, 7, inf, 8, 0, 4, inf, inf, 0, inf },
			{ inf, inf, inf, inf, inf, inf, inf, 0, inf, inf, inf, 9 },
			{ 11, inf, inf, inf, inf, 4, inf, inf, 0, inf, inf, 2 },
			{ inf, inf, inf, inf, 1, inf, inf, inf, inf, 0, inf, inf },
			{ inf, inf, inf, inf, inf, inf, inf, 2, inf, 1, 0, inf },
			{ inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 0 }
		};
		
		int[][] previousPeaks = new int[array.length][array[0].length];
		for ( int i = 0; i < previousPeaks.length; i++ ) {
			for ( int i2 = 0; i2 < previousPeaks[0].length; i2++ ) {
				previousPeaks[i][i2] = i2;
			}
		}
		
		outArray( array );
		
		for ( int k = 0; k < array.length; k++ ) {
			for ( int i = 0; i < array.length; i++ ) {
				for ( int j = 0; j < array[0].length; j++ ) {
					if (( array[i][k] < inf ) & ( array[k][j] < inf )) {
						int d = array[i][k] + array[k][j];
						if ( array[i][j] > d ) {
							array[i][j] = d;
							previousPeaks[i][j] = previousPeaks[i][k];
						}
					}
				}
			}
			outArray( array );
		}
		
		getPath( previousPeaks, 0, 11 );
	}
	
	//получить вершины через которые протекает путь
	public static void getPath( int[][] previousPeaks, int start, int end ) {
		System.out.print( start + " " );
		while ( start != end ) {
			start = previousPeaks[start][end];
			System.out.print( start + " " );
		}
	}
	
	public static void outArray( int[][] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			for ( int i2 = 0; i2 < array[0].length; i2++ ) {
				if ( array[i][i2] == inf ) {
					System.out.print( "inf " );
				} else {
					System.out.print( array[i][i2] + " " );
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}