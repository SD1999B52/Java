import java.util.Scanner;
public class example16_11{
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
System.out.print("������� �������� ������: ");
String mesats = in.nextLine();
System.out.print("������� ���������� ���� � ������: ");
int cdvm = in.nextInt();
System.out.printf("� %s %d ���� \n", mesats, cdvm);
in.close();
}
}