import java.util.Scanner;
public class example16_04{
public static void main(String[] args) {
Scanner InCMD = new Scanner(System.in);
System.out.print("������� �����:");
int num = InCMD.nextInt();
System.out.printf("���� �����: %d \n", num);
InCMD.close();
}
}