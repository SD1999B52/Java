import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class barley_break {
	static int[][] map;
	
	static JPanel panel;
	static int selectX = 0, selectY = 0;
	static int iteration = 1000, columns = 4, lines = 4;
	
	static char showMode = 'd';
	static Timer messageTimer;
	
	public static void main( String[] args ) {
		JFrame app = new JFrame();
		app.setTitle( "Barley Break" );
		app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		app.setSize( 800, 600 );
		app.setMinimumSize( new Dimension( 240, 240 ));
		
		getSettings();
		
		File mapFile = new File( "map.txt" );
		if ( mapFile.exists() == true ) {
			loadGame();
			if ( isValid() == true ) {
				showMode = 'w';
			}
		} else {
			initialMap();
			generationGame();
		}
		
		formCenter( app );
		
		panel = new JPanel() {
			public void paintComponent( Graphics g ) {
				super.paintComponent( g );
				
				//начальный цвет панели
				g.setColor( new Color( 255, 255, 255 ));
				g.fillRect( 0, 0, getPanelWidth(), getPanelHeight());
				
				for ( int i = 0; i < map.length; i++ ) {
					for ( int i2 = 0; i2 < map[0].length; i2++ ) {
						int offsetX = i2 * getSizeCell() + 5;
						int offsetY = i * getSizeCell() + 5;
						
						//отрисовка ячеек
						g.setColor( new Color( 0, 0, 0 ));
						g.drawRect( offsetX, offsetY, getSizeCell(), getSizeCell());
						
						//выделение ячейки под курсором
						if (( selectX == i2 ) & ( selectY == i )) {
							g.drawRect( offsetX - 1, offsetY - 1, getSizeCell() + 2, getSizeCell() + 2 );
						}
						
						//шрифт и размер шрифта текста
						int maxValue = map.length * map[0].length;
						int sizeFont = getSizeCell() / Integer.toString( maxValue ).length();
						g.setFont( new Font( "monospace", Font.BOLD, sizeFont ));
						
						//высота и длинна текста относительно шрифта
						int textWidth = g.getFontMetrics().stringWidth( Integer.toString( map[i][i2] ));
						int textHeigth = g.getFontMetrics().getHeight();
						
						//отступ при отрисовке текста
						int offsetXText = offsetX + ( getSizeCell() - textWidth ) / 2;
						int offsetYText = offsetY + getSizeCell() - ( getSizeCell() - textHeigth / 2 ) / 2;
						
						if ( map[i][i2] != 0 ) {
							g.drawString( Integer.toString( map[i][i2] ), offsetXText, offsetYText );
						}
					}
				}
				
				//показать сообщение
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
					
					g.setFont( new Font( "monospace", Font.BOLD, getSizeCell() - getSizeCell() / 5 ));
					int textWidth = g.getFontMetrics().stringWidth( text );
					int textHeigth = g.getFontMetrics().getHeight();
					
					int x1Board = map[0].length * getSizeCell() / 2 - textWidth / 2 + 5;
					int y1Board = map.length * getSizeCell() / 2 - textHeigth / 2 + 5;
					int x2Board = textWidth;
					int y2Board = textHeigth;
					
					g.setColor( new Color( 255, 255, 255 ));
					g.fillRect( x1Board, y1Board, x2Board, y2Board );
					
					g.setColor( new Color( 0, 0, 0 ));
					g.drawRect( x1Board, y1Board, x2Board, y2Board );
					g.drawRect( x1Board - 1, y1Board - 1, x2Board + 2, y2Board + 2 );
					
					int xText = map[0].length * getSizeCell() / 2 - textWidth / 2 + 5;
					int yText = map.length * getSizeCell() / 2 + textHeigth / 3 + 5;
					g.drawString( text, xText, yText );
				}
			}
		};
		
		panel.addMouseMotionListener( new MouseAdapter() {
			public void mouseMoved( MouseEvent e ) {
				getMouseXCell( e.getX());
				getMouseYCell( e.getY());
				panel.repaint();
			}
			
			public void mouseDragged( MouseEvent e ) {
				getMouseXCell( e.getX());
				getMouseYCell( e.getY());
				panel.repaint();
			}
		});
		
		panel.addMouseListener( new MouseAdapter() {
			public void mouseReleased( MouseEvent e ) {
				if ( showMode == 'd' ) {
					move( selectX, selectY );
					if ( isValid() == true ) {
						showMode = 'w';
					}
				}
				panel.repaint();
			}
		});
		
		panel.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				//кнопка 0
				if ( e.getKeyCode() == 48 ) {
					initialMap();
					generationGame();
					
					showMode = 'd';
				}
				
				//стрелки
				if ( e.getKeyCode() == 38 ) {
					if ( selectY > 0 ) {
						selectY -= 1;
					}
				}
				
				if ( e.getKeyCode() == 40 ) {
					if ( selectY < map.length - 1 ) {
						selectY += 1;
					}
				}
				
				if ( e.getKeyCode() == 37 ) {
					if ( selectX > 0 ) {
						selectX -= 1;
					}
				}
				
				if ( e.getKeyCode() == 39 ) {
					if ( selectX < map[0].length - 1 ) {
						selectX += 1;
					}
				}
				
				//ввод
				if ( e.getKeyCode() == e.VK_ENTER ) {
					if ( showMode == 'd' ) {
						move( selectX, selectY );
						if ( isValid() == true ) {
							showMode = 'w';
						}
					}
				}
				
				//кнопка +
				if ( e.getKeyCode() == 61 ) {
					saveGame();
					
					showMode = 's';
					messageTimer.restart();
				}
				
				//кнопка -
				if ( e.getKeyCode() == 45 ) {
					File mapFile = new File( "map.txt" );
					if ( mapFile.exists() == true ) {
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
				if ( isValid() == true ) {
					showMode = 'w';
				} else {
					showMode = 'd';
				}
				panel.repaint();
			}
		};
		
		//инициализация таймера без повторов для показа сообщений
		messageTimer = new Timer( 5000, listener );
		messageTimer.setRepeats( false );
	}
	
	//получить настройки
	public static void getSettings() {
		File settingsFile = new File( "settings.txt" );
		if ( settingsFile.exists() == true ) {
			try {
				BufferedReader readFile = new BufferedReader( new FileReader( settingsFile ));
				String line = readFile.readLine();
				for ( int i = 0; line != null; i++ ) {
					String[] inLine = line.split( ": " );
					if ( i == 4 ) {
						iteration = Integer.parseInt( inLine[1] );
					}
					if ( i == 5 ) {
						lines = Integer.parseInt( inLine[1] );
					}
					if ( i == 6 ) {
						columns = Integer.parseInt( inLine[1] );
					}
					line = readFile.readLine();
				}
				readFile.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedWriter writeFile = new BufferedWriter( new FileWriter( settingsFile ));
				writeFile.write( "Select: mouse(left button, right button), key(up, down, left, right, enter)" + "\r\n" );
				writeFile.write( "New game key: 0" + "\r\n" );
				writeFile.write( "Save game key: =" + "\r\n" );
				writeFile.write( "Load game key: -" + "\r\n" );
				writeFile.write( "Number of iterations with stirring: 1000" + "\r\n" );
				writeFile.write( "Number of lines: 4" + "\r\n" );
				writeFile.write( "Number of columns: 4" + "\r\n" );
				writeFile.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
	}
	
	//метод сохранения игры
	public static void saveGame() {
		try {
			BufferedWriter writeFile = new BufferedWriter( new FileWriter( "map.txt" ));
			for ( int i = 0; i < map.length; i++ ) {
				for ( int i2 = 0; i2 < map[0].length; i2++ ) {
					writeFile.write( map[i][i2] + " " );
				}
				writeFile.newLine();
			}
			writeFile.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	//метод загрузки сохраненной игры
	public static void loadGame() {
		try {
			ArrayList<String> linesFromFile = new ArrayList<String>();
			BufferedReader readFile = new BufferedReader( new FileReader( "map.txt" ));
			String line = readFile.readLine();
			String[] inLineForNum = line.split( " " );
			int numColumns = inLineForNum.length;
			while ( line != null ) {
				linesFromFile.add( line );
				line = readFile.readLine();
			}
			int numLines = linesFromFile.size();
			readFile.close();
			
			map = new int[numLines][numColumns];
			for ( int i = 0; i < linesFromFile.size(); i++ ) {
				String[] inLine = linesFromFile.get( i ).split( " " );
				for ( int i2 = 0; i2 < inLine.length; i2++ ) {
					map[i][i2] = Integer.parseInt( inLine[i2] );
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	//получить ячейку под курсором(столбец)
	public static void getMouseXCell( int mouseX ) {
		selectX = ( mouseX - 5 ) / getSizeCell();
	}
	
	//получить ячейку под курсором(строка)
	public static void getMouseYCell( int mouseY ) {
		selectY = ( mouseY - 5 ) / getSizeCell();
	}
	
	//получить размер ячейки
	public static int getSizeCell() {
		int wrectOffset = getPanelWidth() - 10;
		int hrectOffset = getPanelHeight() - 10;
		if (( wrectOffset / map[0].length ) > ( hrectOffset / map.length )) {
			return hrectOffset / map.length;
		}
		return wrectOffset / map[0].length;
	}
	
	//получить ширину панели
	public static int getPanelWidth() {
		return panel.getSize().width;
	}
	
	//получить высоту панели
	public static int getPanelHeight() {
		return panel.getSize().height;
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
	
	//перемещение плитки
	public static void move( int mapX, int mapY ) {
		if ( mapX - 1 >= 0 ) {
			if ( map[mapY][mapX - 1] == 0 ) {
				map[mapY][mapX - 1] = map[mapY][mapX];
				map[mapY][mapX] = 0;
			}
		}
		if ( mapX + 1 < map.length ) {
			if ( map[mapY][mapX + 1] == 0 ) {
				map[mapY][mapX + 1] = map[mapY][mapX];
				map[mapY][mapX] = 0;
			}
		}
		if ( mapY - 1 >= 0 ) {
			if ( map[mapY - 1][mapX] == 0 ) {
				map[mapY - 1][mapX] = map[mapY][mapX];
				map[mapY][mapX] = 0;
			}
		}
		if ( mapY + 1 < map[0].length ) {
			if ( map[mapY + 1][mapX] == 0 ) {
				map[mapY + 1][mapX] = map[mapY][mapX];
				map[mapY][mapX] = 0;
			}
		}
	}
	
	//проверка правильности сборки
	public static boolean isValid() {
		int num = 1;
		int maxNum = map.length * map[0].length;
		for ( int i = 0; i < map.length; i++ ) {
			for ( int i2 = 0; i2 < map[0].length; i2++ ) {
				if (( map[i][i2] != num ) & ( num < maxNum )) {
					return false;
				}
				num += 1;
			}
		}
		return true;
	}
	
	//генерация игры
	public static void generationGame() {
		int mapX = map[0].length - 1;
		int mapY = map.length - 1;
		for ( int i = 0; i < iteration; i++ ) {
			
			ArrayList<Integer> accessibleSides = new ArrayList<Integer>();
			if ( mapX - 1 >= 0 ) {
				accessibleSides.add( 1 );//left
			}
			if ( mapX + 1 < map[0].length ) {
				accessibleSides.add( 2 );//right
			}
			if ( mapY - 1 >= 0 ) {
				accessibleSides.add( 3 );//up
			}
			if ( mapY + 1 < map.length ) {
				accessibleSides.add( 4 );//down
			}
			
			int index = genNumber( 0, accessibleSides.size());
			int side = accessibleSides.get( index );
			
			if ( side == 1 ) {
				map[mapY][mapX] = map[mapY][mapX - 1];
				map[mapY][mapX - 1] = 0;
				mapX -= 1;
			}
			if ( side == 2 ) {
				map[mapY][mapX] = map[mapY][mapX + 1];
				map[mapY][mapX + 1] = 0;
				mapX += 1;
			}
			if ( side == 3 ) {
				map[mapY][mapX] = map[mapY - 1][mapX];
				map[mapY - 1][mapX] = 0;
				mapY -= 1;
			}
			if ( side == 4 ) {
				map[mapY][mapX] = map[mapY + 1][mapX];
				map[mapY + 1][mapX] = 0;
				mapY += 1;
			}
		}
	}
	
	//инициализация массива и заполнение его значениями 1-15
	public static void initialMap() {
		map = new int[lines][columns];
		int num = 1;
		for ( int i = 0; i < map.length; i++ ) {
			for ( int i2 = 0; i2 < map[0].length; i2++ ) {
				map[i][i2] = num;
				num += 1;
			}
		}
		map[map.length - 1][map[0].length - 1] = 0;
	}
	
	//генерация числа в диапазоне от min до max не включительно
	public static int genNumber( int min, int max ) {
		return (int)( Math.random() * ( max - min ) + min );
	}
}