package ex_5;
import java.util.Scanner;

public class lessons1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner( System.in );
		System.out.print("������� �����: ");
		int number = in.nextInt();
		int otvet = (int)( number / 1000 );
		System.out.printf("�����: � ����� %d %d �����", number, otvet );
		in.close();
	}

}
