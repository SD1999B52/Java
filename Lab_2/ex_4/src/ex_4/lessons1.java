package ex_4;
import java.util.Scanner;

public class lessons1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Scanner in = new Scanner( System.in );
			System.out.print("������� �����: ");
			int number = in.nextInt();
			if (( number >= 5 ) & ( number <= 10 )) { 
				System.out.printf("����� %d �������������", number );
			} else {
				System.out.printf("����� %d ���������������", number );
			}
			in.close();
	}

}
