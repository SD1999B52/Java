import java.util.Scanner;
public class example16_09{
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
System.out.print("������� ���: ");
String name = in.nextLine();
System.out.print("������� �������: ");
int age = in.nextInt();
System.out.printf("���: %s �������: %d \n", name, age);
in.close();
}
}