import java.util.Scanner;
public class example16_10{
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
System.out.print("������� �������� �������� ��� ������: ");
String ntdn = in.nextLine();
System.out.print("������� �������� �������� ������: ");
String mesats = in.nextLine();
System.out.print("������� ����� ��� ������: ");
int ndm = in.nextInt();
System.out.printf("%s %d %s \n", ntdn, ndm, mesats);
in.close();
}
}