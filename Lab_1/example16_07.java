import java.util.Scanner;
public class example16_07{
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
System.out.print("������ �����: ");
int radius = in.nextInt();
long area = Math.round(Math.PI * Math.pow(radius, 2));
System.out.printf("S ����� � R %d = %d \n", radius, area);
}
}