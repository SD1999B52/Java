import java.util.Scanner;
public class example16_08{
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
System.out.print("������� F: ");
String f = in.nextLine();
System.out.print("������� N: ");
String n = in.nextLine();
System.out.print("������� P: ");
String p = in.nextLine();
System.out.printf("������ %s %s %s \n", f, n, p);
in.close();
}
}