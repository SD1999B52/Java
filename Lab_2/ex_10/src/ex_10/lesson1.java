package ex_10;
import java.util.Scanner;
import java.lang.String;

public class lesson1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner( System.in );
		System.out.print("������� �����: ");
		int number = in.nextInt();
		String convert = Integer.toOctalString( number );
		String otvet = convert.substring( convert.length() - 1 );
		System.out.printf("����� � ����������: %s � �����������: %s ������ ������: %s", number, convert, otvet  );
		in.close();
	}

}
