package ex_1;
import java.util.Scanner;

public class lesson1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner( System.in );
		System.out.print("������� �����: ");
		int number = in.nextInt();
		if ( number % 3 != 0 ) { 
			System.out.printf("����� %d �� ������� �� 3", number );
		} else {
			System.out.printf("����� %d ������� �� 3", number );
		}
		in.close();
	}

}
