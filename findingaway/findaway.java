/*
Основа - Алгоритм Ли
Ортогональный путь с доп. выравниванием
# - преграда
. - дорога
@ - начальная точка
X - конечная точка
*/

import java.util.ArrayList;

class findaway {
	public static void main( String[] args ) {
		char[][] map1 = {
			{ '.', '.', '.', '@', '.' },
			{ '.', '#', '#', '#', '#' },
			{ '.', '.', '.', '.', '.' },
			{ '#', '#', '#', '#', '.' },
			{ '.', 'X', '.', '.', '.' }
		};
		
		char[][] map2 = {
			{ '.', '.', 'X', '.', '.' },
			{ '#', '#', '#', '#', '#' },
			{ '.', '.', '.', '.', '.' },
			{ '.', '@', '.', '.', '.' },
			{ '.', '.', '.', '.', '.' }
		};
		
		char[][] map3 = {
			{ '.', '.', '.', '.', '@' },
			{ '#', '.', '#', '#', '#' },
			{ '.', '.', '.', '.', '.' },
			{ '.', '.', '.', '.', 'X' },
			{ '.', '.', '.', '.', '.' }
		};
		
		char[][] map4 = {
			{ '.', '.', '.', '#', '@', '.', '#' },
			{ '#', '.', '.', '.', '#', '.', '.' },
			{ 'X', '#', '#', '.', '.', '#', '.' },
			{ '.', '.', '.', '#', '.', '.', '.' },
			{ '.', '#', '.', '.', '.', '.', '.' }
		};
		
		char[][] route = findRoute( map4 );
		if ( route != null ) {
			out( route );
		} else {
			System.out.println( "null" );
		}
	}
	
	public static char[][] findRoute( char[][] map ) {
		//нахождение координат начала и конца
		int xStart = 0, yStart = 0;
		int xEnd = 0, yEnd = 0;
		for ( int i = 0; i < map.length; i++ ) {
			for ( int i2 = 0; i2 < map[0].length; i2++ ) {
				if ( map[i][i2] == '@' ) {
					xStart = i2;
					yStart = i;
				}
				if ( map[i][i2] == 'X' ) {
					xEnd = i2;
					yEnd = i;
				}
			}
		}
		
		//распространение волны
		int[][] mapwave = new int[map.length][map[0].length];
		mapwave[yStart][xStart] = 1;
		boolean wavePropagation = true;
		while ( wavePropagation == true ) {
			wavePropagation = false;
			for ( int i = 0; i < mapwave.length; i++ ) {
				for ( int i2 = 0; i2 < mapwave[0].length; i2++ ) {
					if ( mapwave[i][i2] != 0 ) {
						if ( i - 1 >= 0 ) {
							if (( mapwave[i - 1][i2] == 0 ) & ( map[i - 1][i2] != '#' )) {
								mapwave[i - 1][i2] = mapwave[i][i2] + 1;
								wavePropagation = true;
							}
						}
						if ( i + 1 < mapwave.length ) {
							if (( mapwave[i + 1][i2] == 0 ) & ( map[i + 1][i2] != '#' )) {
								mapwave[i + 1][i2] = mapwave[i][i2] + 1;
								wavePropagation = true;
							}
						}
						if ( i2 - 1 >= 0 ) {
							if (( mapwave[i][i2 - 1] == 0 ) & ( map[i][i2 - 1] != '#' )) {
								mapwave[i][i2 - 1] = mapwave[i][i2] + 1;
								wavePropagation = true;
							}
						}
						if ( i2 + 1 < mapwave[0].length ) {
							if (( mapwave[i][i2 + 1] == 0 ) & ( map[i][i2 + 1] != '#' )) {
								mapwave[i][i2 + 1] = mapwave[i][i2] + 1;
								wavePropagation = true;
							}
						}
					}
				}
			}
		}
		
		//восстановление пути
		if ( mapwave[yEnd][xEnd] != 0 ) {
			ArrayList<char[][]> mapsSides = new ArrayList<char[][]>();
			char[][] mapSave = copy( map );
			int[] turns = new int[4];
			
			for ( int startSide = 0; startSide < 4; startSide++ ) {
				int x = xEnd, y = yEnd;
				char side = 'l';
				if ( startSide == 1 ) {
					side = 'r';
				}
				if ( startSide == 2 ) {
					side = 'u';
				}
				if ( startSide == 3 ) {
					side = 'd';
				}
				
				while ( mapwave[y][x] != 2 ) {
					boolean left = false, right = false, up = false, down = false;
					
					if ( x - 1 >= 0 ) {
						if ( mapwave[y][x] - 1 == mapwave[y][x - 1] ) {
							left = true;
						}
					}
					if ( x + 1 < mapwave[0].length ) {
						if ( mapwave[y][x] - 1 == mapwave[y][x + 1] ) {
							right = true;
						}
					}
					if ( y - 1 >= 0 ) {
						if ( mapwave[y][x] - 1 == mapwave[y - 1][x] ) {
							up = true;
						}
					}
					if ( y + 1 < mapwave.length ) {
						if ( mapwave[y][x] - 1 == mapwave[y + 1][x] ) {
							down = true;
						}
					}
				
					//если есть возможность двигаться дальше в ту же сторону
					if (( left == true ) & ( side == 'l' )) {
						x -= 1;
						map[y][x] = '+';
						continue;
					}
					if (( right == true ) & ( side == 'r' )) {
						x += 1;
						map[y][x] = '+';
						continue;
					}
					if (( up == true ) & ( side == 'u' )) {
						y -= 1;
						map[y][x] = '+';
						continue;
					}
					if (( down == true ) & ( side == 'd' )) {
						y += 1;
						map[y][x] = '+';
						continue;
					}
				
					//если поворот неизбежен
					if ( left == true ) {
						turns[startSide] += 1;
						side = 'l';
						continue;
					}
					if ( right == true ) {
						turns[startSide] += 1;
						side = 'r';
						continue;
					}
					if ( up == true ) {
						turns[startSide] += 1;
						side = 'u';
						continue;
					}
					if ( down == true ) {
						turns[startSide] += 1;
						side = 'd';
						continue;
					}
				}
				
				//сохраняем путь и сбрасываем map до начального состояния
				mapsSides.add( map );
				map = copy( mapSave );
			}
			
			//находим минимальный по поворотам маршрут
			int indexMin = 0;
			for ( int i = 1; i < turns.length; i++ ) {
				if ( turns[i] < turns[indexMin] ) {
					indexMin = i;
				}
			}
			return mapsSides.get( indexMin );
		}
		return null;
	}
	
	//метод вывода значений массива
	public static void out( char[][] map ) {
		for ( int i = 0; i < map.length; i++ ) {
			for ( int i2 = 0; i2 < map[0].length; i2++ ) {
				System.out.print( map[i][i2] );
			}
			System.out.println();
		}
	}
	
	//метод копирования значений массива
	public static char[][] copy( char[][] array ) {
		char[][] newArray = new char[array.length][array[0].length];
		for ( int i = 0; i < array.length; i++ ) {
			System.arraycopy( array[i], 0, newArray[i], 0, array[i].length );
		}
		return newArray;
	}
}