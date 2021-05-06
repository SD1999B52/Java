import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class sudoku {
	static int[][] array = new int[9][9];
	static int[][] viewArray = new int[9][9];
	static int[][] playerArray = new int[9][9];
	static int selectX = 0, selectY = 0;
	static int level = 65;
	static JPanel panel;
	
	static char showMode = 'd';
	static Timer messageTimer;
	
	public static void main( String[] args ) {
		JFrame app = new JFrame();
		app.setTitle( "Sudoku" );
		app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		app.setSize( 800, 600 );
		app.setMinimumSize( new Dimension( 240, 240 ));
		
		formCenter( app );
		
		getLevelOrCreateReadMe();
		
		if ( saveExists() == true ) {
			loadGame();
			if ( gameWin() == true ) {
				showMode = 'w';
			}
		} else {
			newGame();
		}
		
		panel = new JPanel() {
			public void paintComponent( Graphics g ) {
				super.paintComponent( g );
				g.setColor( new Color( 255, 255, 255 ));
				g.fillRect( 0, 0, getPanelWidth(), getPanelHeight());
				g.setFont( new Font( "monospace", Font.BOLD, getCellSize()));
				int offsetXSave = 0, offsetYSave = 0;
				for ( int i = 0; i < 9; i++ ) {
					for ( int i2 = 0; i2 < 9; i2++ ) {
						int offsetX = 5 + i * getCellSize() + i / 3 * 5;
						int offsetY = 5 + i2 * getCellSize() + i2 / 3 * 5;
						
						if ( viewArray[i2][i] == 0 ) {
							g.setColor( new Color( 223, 255, 214 ));
							g.fillRect( offsetX, offsetY, getCellSize(), getCellSize());
						}
						
						//подсветка строки и столбца
						if (( selectY == i2 ) | ( selectX == i )) {
							g.setColor( new Color( 0, 255, 0 ));
							g.fillRect( offsetX, offsetY, getCellSize(), getCellSize());
						}
						
						//подсветка выбранного значения
						g.setColor( new Color( 255, 0, 0 ));
						if ( viewArray[selectY][selectX] != 0 ) {
							if (( viewArray[selectY][selectX] == viewArray[i2][i] ) | ( viewArray[selectY][selectX] == playerArray[i2][i] )) {
								g.fillRect( offsetX, offsetY, getCellSize(), getCellSize());
							}
						}
						if ( playerArray[selectY][selectX] != 0 ) {
							if (( playerArray[selectY][selectX] == playerArray[i2][i] ) | ( playerArray[selectY][selectX] == viewArray[i2][i] )) {
								g.fillRect( offsetX, offsetY, getCellSize(), getCellSize());
							}
						}
						
						g.setColor( new Color( 0, 0, 0 ));
						g.drawRect( offsetX, offsetY, getCellSize(), getCellSize());
						
						int textWidth = g.getFontMetrics().stringWidth( Integer.toString( viewArray[i2][i] ));
						int textHeigth = g.getFontMetrics().getHeight();
						
						int offsetXText = offsetX + ( getCellSize() - textWidth ) / 2;
						int offsetYText = offsetY + getCellSize() + ( getCellSize() - textHeigth ) / 2;
						
						if ( viewArray[i2][i] != 0 ) {
							g.drawString( Integer.toString( viewArray[i2][i] ), offsetXText, offsetYText );
						}
						
						if ( playerArray[i2][i] != 0 ) {
							g.drawString( Integer.toString( playerArray[i2][i] ), offsetXText, offsetYText );
						}
						
						if (( selectX == i ) & ( selectY == i2 )) {
							offsetXSave = offsetX;
							offsetYSave = offsetY;
						}
					}
				}
				
				if (( offsetXSave != 0 ) & ( offsetYSave != 0 )) {
					g.drawRect( offsetXSave - 1, offsetYSave - 1, getCellSize() + 2, getCellSize() + 2 );
					g.drawRect( offsetXSave - 2, offsetYSave - 2, getCellSize() + 4, getCellSize() + 4 );
				}
				
				if ( showMode != 'd' ) {
					String text = "";
					
					if ( showMode == 's' ) {
						text = "Save";
					}
					if ( showMode == 'l' ) {
						text = "Load";
					}
					if ( showMode == 'w' ) {
						text = "You Win";
					}
					
					g.setFont( new Font( "monospace", Font.BOLD, getCellSize() * 2 ));
					int textWidth = g.getFontMetrics().stringWidth( text );
					int textHeigth = g.getFontMetrics().getHeight();
					
					int x1Board = 9 * getCellSize() / 2 - textWidth / 2 + 5;
					int y1Board = 9 * getCellSize() / 2 - textHeigth / 2 + 5;
					int x2Board = textWidth;
					int y2Board = textHeigth;
					
					g.setColor( new Color( 255, 255, 255 ));
					g.fillRect( x1Board, y1Board, x2Board, y2Board );
					
					g.setColor( new Color( 0, 0, 0 ));
					g.drawRect( x1Board, y1Board, x2Board, y2Board );
					g.drawRect( x1Board - 1, y1Board - 1, x2Board + 2, y2Board + 2 );
					
					int xText = 9 * getCellSize() / 2 - textWidth / 2 + 5;
					int yText = 9 * getCellSize() / 2 + textHeigth / 3 + 5;
					g.drawString( text, xText, yText );
				}
			}
		};
		
		panel.addMouseMotionListener( new MouseAdapter() {
			public void mouseMoved( MouseEvent e ) {
				selectX = getMouseXCell( e.getX());
				selectY = getMouseYCell( e.getY());
				panel.repaint();
			}
			public void mouseDragged( MouseEvent e ) {
				selectX = getMouseXCell( e.getX());
				selectY = getMouseYCell( e.getY());
				panel.repaint();
			}
		});
		
		panel.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				if (( viewArray[selectY][selectX] == 0 ) & ( showMode == 'd' )) {
					for ( int i = 49; i <= 57; i++ ) {
						if ( e.getKeyCode() == i ) {
							playerArray[selectY][selectX] = Character.getNumericValue( i );
						}
					}
					if ( e.getKeyCode() == 8 ) {
						playerArray[selectY][selectX] = 0;
					}
					if ( gameWin() == true ) {
						showMode = 'w';
					}
				}
				if ( e.getKeyCode() == 48 ) {
					newGame();
					
					showMode = 'd';
				}
				if ( e.getKeyCode() == 39 ) {
					if ( selectX < 8 ) {
						selectX += 1;
					}
				}
				if ( e.getKeyCode() == 37 ) {
					if ( selectX > 0 ) {
						selectX -= 1;
					}
				}
				if ( e.getKeyCode() == 38 ) {
					if ( selectY > 0 ) {
						selectY -= 1;
					}
				}
				if ( e.getKeyCode() == 40 ) {
					if ( selectY < 8 ) {
						selectY += 1;
					}
				}
				if ( e.getKeyCode() == 61 ) {
					saveGame();
					
					showMode = 's';
					messageTimer.restart();
				}
				if ( e.getKeyCode() == 45 ) {
					if ( saveExists() == true ) {
						loadGame();
						
						showMode = 'l';
						messageTimer.restart();
					}
				}
				
				panel.repaint();
			}
		});
		
		panel.setFocusable( true );
		panel.requestFocus();
		
		app.add( panel, BorderLayout.CENTER );
		app.setVisible( true );
		
		ActionListener listener = new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				if ( gameWin() == true ) {
					showMode = 'w';
				} else {
					showMode = 'd';
				}
				panel.repaint();
			}
		};
		
		//инициализация таймера без повторов для показа сообщений
		messageTimer = new Timer( 2000, listener );
		messageTimer.setRepeats( false );
	}
	
	//метод перемещения формы в центр экрана
	public static void formCenter( JFrame form ) {
		int formWidth = (int)form.getBounds().getSize().getWidth();
		int formHeight = (int)form.getBounds().getSize().getHeight();
		int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int newFormX = screenWidth / 2 - formWidth / 2;
		int newFormY = screenHeight / 2 - formHeight / 2;
		form.setBounds( newFormX, newFormY, formWidth, formHeight );
	}
	
	//метод загрузки сохраненной игры и создание ключа
	public static void loadGame() {
		try {
			BufferedReader readFile1 = new BufferedReader( new FileReader( "viewArray.txt" ));
			BufferedReader readFile2 = new BufferedReader( new FileReader( "playerArray.txt" ));
			for ( int i = 0; i < 9; i++ ) {
				String line1 = readFile1.readLine();
				String line2 = readFile2.readLine();
				for ( int i2 = 0; i2 < 9; i2++ ) {
					viewArray[i][i2] = Character.getNumericValue( line1.charAt( i2 ));
					playerArray[i][i2] = Character.getNumericValue( line2.charAt( i2 ));
				}
			}
			readFile1.close();
			readFile2.close();
			
			arrayDataCopy( array, viewArray );
			solve( array );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	//метод проверки существования сохраненной игры
	public static boolean saveExists() {
		File file1 = new File( "viewArray.txt" );
		File file2 = new File( "playerArray.txt" );
		if (( file1.exists() == true ) & ( file2.exists() == true )) {
			return true;
		}
		return false;
	}
	
	//метод сохранения игры
	public static void saveGame() {
		try {
			BufferedWriter writeFile1 = new BufferedWriter( new FileWriter( "viewArray.txt" ));
			BufferedWriter writeFile2 = new BufferedWriter( new FileWriter( "playerArray.txt" ));
			for ( int i = 0; i < 9; i++ ) {
				for ( int i2 = 0; i2 < 9; i2++ ) {
					writeFile1.write( viewArray[i][i2] + "" );
					writeFile2.write( playerArray[i][i2] + "" );
				}
				writeFile1.newLine();
				writeFile2.newLine();
			}
			writeFile1.close();
			writeFile2.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	//метод создания файла readMe.txt или чтение Difficulty - количества шагов при удалении значений из ключа
	public static void getLevelOrCreateReadMe() {
		try {
			File file = new File( "readMe.txt" );
			if ( file.exists() == true ) {
				BufferedReader readFile = new BufferedReader( new FileReader( file ));
				String line = "";
				for ( int i = 0; i < 6; i++ ) {
					line = readFile.readLine();
				}
				
				String[] data = line.split( ":" );
				data[1] = data[1].replaceAll( " ", "" );
				
				readFile.close();
				level = Integer.parseInt( data[1] );
			} else {
				BufferedWriter writeFile = new BufferedWriter( new FileWriter( file ));
				writeFile.write( "Value key: 1,2,3,4,5,6,7,8,9" + "\r\n" );
				writeFile.write( "New game key: 0" + "\r\n" );
				writeFile.write( "Select: mouse, key: up, down, left, right" + "\r\n" );
				writeFile.write( "Save game key: =" + "\r\n" );
				writeFile.write( "Load game key: -" + "\r\n" );
				writeFile.write( "Difficulty ( 0 - 65 ): 65" + "\r\n" );
				writeFile.close();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	//метод начала новой игры
	public static void newGame() {
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				array[i][i2] = 0;
				viewArray[i][i2] = 0;
				playerArray[i][i2] = 0;
			}
		}
		fillInTable();
		generateTable();
		delValue();
	}
	
	//метод объединения судоку с ответами игрока и проверка совпадения с ключом
	public static boolean gameWin() {
		int[][] newarray = new int[9][9];
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				newarray[i][i2] = viewArray[i][i2];
				if ( viewArray[i][i2] == 0 ) {
					newarray[i][i2] = playerArray[i][i2];
				}
			}
		}
		return compareOfArrays( array, newarray );
	}
	
	//получить ячейку под курсором(столбец)
	public static int getMouseXCell( int mouseX ) {
		int mouseXOffset = ( mouseX - 5 ) / getCellSize() / 3 * 5;
		int mouseXCell = ( mouseX - 5 - mouseXOffset ) / getCellSize();
		if ( mouseXCell > 8 ) {
			mouseXCell = 8;
		}
		if ( mouseXCell < 0 ) {
			mouseXCell = 0;
		}
		return mouseXCell;
	}
	
	//получить ячейку под курсором(строка)
	public static int getMouseYCell( int mouseY ) {
		int mouseYOffset = ( mouseY - 5 ) / getCellSize() / 3 * 5;
		int mouseYCell = ( mouseY - 5 - mouseYOffset ) / getCellSize();
		if ( mouseYCell > 8 ) {
			mouseYCell = 8;
		}
		if ( mouseYCell < 0 ) {
			mouseYCell = 0;
		}
		return mouseYCell;
	}
	
	//получить размер ячейки
	public static int getCellSize() {
		int wrectOffset = getPanelWidth() - 5 * 4;
		int hrectOffset = getPanelHeight() - 5 * 4;
		if (( wrectOffset / 9 ) > ( hrectOffset / 9 )) {
			return hrectOffset / 9;
		}
		return wrectOffset / 9;
	}
	
	//получить высоту панели
	public static int getPanelHeight() {
		return panel.getSize().height;
	}
	
	//получить ширину панели
	public static int getPanelWidth() {
		return panel.getSize().width;
	}
	
	/*------------------------------------
	далее методы создания матрицы судоку
	------------------------------------*/
	//метод удаления значений и проверка на одно решение
	public static void delValue() {
		int[][] notEmpty = new int[2][81];
		int[][] ban = new int[9][9];
		
		arrayDataCopy( viewArray, array );
		
		for ( int inum = 0; inum < level; inum++ ) {
			int index = 0;
			for ( int i = 0; i < 9; i++ ) {
				for ( int i2 = 0; i2 < 9; i2++ ) {
					if (( viewArray[i][i2] != 0 ) & ( ban[i][i2] == 0 )) {
						notEmpty[0][index] = i;
						notEmpty[1][index] = i2;
						index += 1;
					}
				}
			}
			
			int indexSelected = (int)( Math.random() * index );
			int cellY = notEmpty[0][indexSelected];
			int cellX = notEmpty[1][indexSelected];
			
			int[][] saveArray = new int[9][9];
			arrayDataCopy( saveArray, viewArray );
			
			viewArray[cellY][cellX] = 0;
			
			if ( oneSolution() == false ) {
				arrayDataCopy( viewArray, saveArray );
				ban[cellY][cellX] = 1;
			}
		}
	}
	
	//метод перемешки значений в таблице
	public static void generateTable() {
		for ( int i = 0; i < 10; i++ ) {
			int operation = (int)( Math.random() * 5 );
			if ( operation == 0 ) {
				transpose();
			}
			if ( operation == 1 ) {
				swapRowsSmall();
			}
			if ( operation == 2 ) {
				swapColumsSmall();
			}
			if ( operation == 3 ) {
				swapRowsArea();
			}
			if ( operation == 4 ) {
				swapColumsArea();
			}
		}
	}
	
	//обмен двух районов по вертикали
	public static void swapColumsArea() {
		transpose();
		swapRowsArea();
		transpose();
	}
	
	//обмен двух районов по горизонтали
	public static void swapRowsArea() {
		int cellYA = (int)( Math.random() * 3 );
		
		int[] options = new int[2];
		int numValue = 0;
		for ( int i = 0; i < 3; i++ ) {
			if ( i != cellYA ) {
				options[numValue] = i;
				numValue += 1;
			}
		}
		int numCellYB = (int)( Math.random() * 2 );
		int cellYB = options[numCellYB];
		
		int[][] newarray = new int[9][9];
		arrayDataCopy( newarray, array );
		
		for ( int i = 0; i < 3; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				newarray[cellYA * 3 + i][i2] = array[cellYB * 3 + i][i2];
				newarray[cellYB * 3 + i][i2] = array[cellYA * 3 + i][i2];
			}
		}
		arrayDataCopy( array, newarray );
	}
	
	//обмен двух столбцов в пределах одного района
	public static void swapColumsSmall() {
		transpose();
		swapRowsSmall();
		transpose();
	}
	
	//обмен двух строк в пределах одного района
	public static void swapRowsSmall() {
		int cellY = (int)( Math.random() * 3 );
		int rowA = (int)( Math.random() * 3 );
		
		int[] options = new int[2];
		int numValue = 0;
		for ( int i = 0; i < 3; i++ ) {
			if ( i != rowA ) {
				options[numValue] = i;
				numValue += 1;
			}
		}
		int numRowB = (int)( Math.random() * 2 );
		int rowB = options[numRowB];
		
		int[][] newarray = new int[9][9];
		arrayDataCopy( newarray, array );
		
		for ( int i = 0; i < 9; i++ ) {
			newarray[cellY * 3 + rowA][i] = array[cellY * 3 + rowB][i];
			newarray[cellY * 3 + rowB][i] = array[cellY * 3 + rowA][i];
		}
		arrayDataCopy( array, newarray );
	}
	
	//транспонированние матрицы (замена строк столбцами)
	public static void transpose() {
		int[][] newarray = new int[9][9];
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				newarray[i][i2] = array[i2][i];
			}
		}
		arrayDataCopy( array, newarray );
	}
	
	//заполнения начальной матрицы со смещение
	public static void fillInTable() {
		for ( int i = 0; i < 9; i++ ) {
			int ycell = i / 3;
			for ( int i2 = 0; i2 < 9; i2++ ) {
				int offset = i * 3;
				array[i][i2] = ( i2 + ycell + offset ) % 9 + 1;
			}
		}
	}
	
	/*------------------------------------------------------------
	далее методы решения судоку для получения количества решений
	------------------------------------------------------------*/
	//метод проверки существования только одного варианта решения
	public static boolean oneSolution() {
		int[][] arraySolA = new int[9][9];
		int[][] arraySolB = new int[9][9];
		arrayDataCopy( arraySolA, viewArray );
		arrayDataCopy( arraySolB, viewArray );
		solve( arraySolA );
		solveB( arraySolB );
		return compareOfArrays( arraySolA, arraySolB );
	}
	
	//метод перебора значений с 9 до 1 с откатом при неверном подобранном значении
	public static boolean solveB( int[][] sudokuArray ) {
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				if ( sudokuArray[i][i2] == 0 ) {
					for ( int n = 9; n > 0; n-- ) {
						sudokuArray[i][i2] = n;
						if ( isValid( sudokuArray, i, i2 ) == true ) {
							if ( solveB( sudokuArray ) == true ) {
								return true;
							}
						}
						sudokuArray[i][i2] = 0;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	//метод проверки равенства двух массивов
	public static boolean compareOfArrays( int[][] array1, int[][] array2 ) {
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				if ( array1[i][i2] != array2[i][i2] ) {
					return false;
				}
			}
		}
		return true;
	}
	
	//метод копирования значений одного массива в другой
	public static void arrayDataCopy( int[][] array1, int[][] array2 ) {
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				array1[i][i2] = array2[i][i2];
			}
		}
	}
	
	/*------------------------------------------------------
	далее методы решения судоку методом поиска с возвратом
	------------------------------------------------------*/
	//метод перебора значений с 1 до 9 с откатом при неверно подобранном значении
	public static boolean solve( int[][] sudokuArray ) {
		for ( int i = 0; i < 9; i++ ) {
			for ( int i2 = 0; i2 < 9; i2++ ) {
				if ( sudokuArray[i][i2] == 0 ) {
					for ( int n = 1; n <= 9; n++ ) {
						sudokuArray[i][i2] = n;
						if ( isValid( sudokuArray, i, i2 ) == true ) {
							if ( solve( sudokuArray ) == true ) {
								return true;
							}
						}
						sudokuArray[i][i2] = 0;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	//метод проверки правильности судоку
	public static boolean isValid( int[][] sudokuArray, int y, int x ) {
		if ( rowConstraint( sudokuArray, y ) == false ) {
			return false;
		}
		if ( columnConstraint( sudokuArray, x ) == false ) {
			return false;
		}
		if ( subsectionConstraint( sudokuArray, y, x ) == false ) {
			return false;
		}
		return true;
	}
	
	//метод проверки что в каждом столбце каждое число встречается по разу
	public static boolean columnConstraint( int[][] sudokuArray, int x ) {
		int[] constraint = new int[9];
		for ( int i = 0; i < 9; i++ ) {
			int n = sudokuArray[i][x] - 1;
			if ( n == -1 ) {
				continue;
			}
			constraint[n] += 1;
			if ( constraint[n] > 1 ) {
				return false;
			}
		}
		return true;
	}
	
	//метод проверки что в каждой строке каждое число встречается по разу
	public static boolean rowConstraint( int[][] sudokuArray, int y ) {
		int[] constraint = new int[9];
		for ( int i = 0; i < 9; i++ ) {
			int n = sudokuArray[y][i] - 1;
			if ( n == -1 ) {
				continue;
			}
			constraint[n] += 1;
			if ( constraint[n] > 1 ) {
				return false;
			}
		}
		return true;
	}
	
	//метод проверки что в каждом квадранте каждое число встречается по разу
	public static boolean subsectionConstraint( int[][] sudokuArray, int y, int x ) {
		int[] constraint = new int[9];
		int rowStart = ( y / 3 ) * 3;
		int rowEnd = ( x / 3 ) * 3;
		for ( int i = 0; i < 3; i++ ) {
			for ( int i2 = 0; i2 < 3; i2++ ) {
				int n = sudokuArray[i + rowStart][i2 + rowEnd] - 1;
				if ( n == -1 ) {
					continue;
				}
				constraint[n] += 1;
				if ( constraint[n] > 1 ) {
					return false;
				}
			}
		}
		return true;
	}
}