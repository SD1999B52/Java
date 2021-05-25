class Floyd_algorithm {
	final static int inf = Integer.MAX_VALUE;
	
	static int[][] matrix = {
		{ 0, 1, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf },
		{ inf, 0, 10, inf, inf, 5, inf, inf, inf, inf, 2, inf },
		{ inf, inf, 0, inf, inf, inf, 3, inf, inf, inf, inf, inf },
		{ inf, inf, 3, 0, inf, inf, 5, inf, inf, inf, inf, 5 },
		{ inf, 5, inf, inf, 0, inf, inf, inf, inf, inf, inf, inf },
		{ inf, inf, inf, inf, 3, 0, 7, inf, inf, inf, inf, inf },
		{ inf, inf, inf, 7, inf, 8, 0, 4, inf, inf, 0, inf },
		{ inf, inf, inf, inf, inf, inf, inf, 0, inf, inf, inf, 9 },
		{ 11, inf, inf, inf, inf, 4, inf, inf, 0, inf, inf, 2 },
		{ inf, inf, inf, inf, 1, inf, inf, inf, inf, 0, inf, inf },
		{ inf, inf, inf, inf, inf, inf, inf, 2, inf, 1, 0, inf },
		{ inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 0 }
	};
	
	public static void main( String[] args ) {
		int[][] ancestorMatrix = new int[matrix.length][matrix[0].length];
		for ( int i = 0; i < ancestorMatrix.length; i++ ) {
			for ( int j = 0; j < ancestorMatrix[0].length; j++ ) {
				ancestorMatrix[i][j] = j;
			}
		}
		
		for ( int k = 0; k < matrix.length; k++ ) {
			for ( int i = 0; i < matrix.length; i++ ) {
				for ( int j = 0; j < matrix[0].length; j++ ) {
					if (( matrix[i][k] < inf ) & ( matrix[k][j] < inf )) {
						if ( matrix[i][j] > matrix[i][k] + matrix[k][j] ) {
							matrix[i][j] = matrix[i][k] + matrix[k][j];
							ancestorMatrix[i][j] = ancestorMatrix[i][k];
						}
					}
				}
			}
			arrayOut( matrix );
			System.out.println();
		}
		
		//восстанавливаем маршрут
		int start = 0, end = 11;
		String way = "";
		while ( start != end ) {
			way += start + " -> ";
			start = ancestorMatrix[start][end];
		}
		System.out.println( "Way: " + way + end );
	}
	
	public static void arrayOut( int[][] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				if ( array[i][j] == inf ) {
					System.out.print( "inf " );
				} else {
					System.out.print( array[i][j] + " " );
				}
			}
			System.out.println();
		}
	}
}