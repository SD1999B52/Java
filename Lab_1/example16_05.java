import java.util.Scanner;
public class example16_05{
public static void main(String[] args) {	
Scanner in = new Scanner(System.in);
System.out.print("������� ���: ");
String name = in.nextLine();
System.out.print("������� �������: ");
int age = in.nextInt();
System.out.print("������� ����: ");
float height = in.nextFloat();
System.out.printf("���: %s �������: %d ����: %.2f \n", name, age, height);
in.close();
}
}