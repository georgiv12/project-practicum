public class mm {

    /**
     * @param args
     */

    public static void main(String[] args) {

// TODO Auto-generated method stub

        int n = 6;

        for (int i = 0; i <= n - 1; i++) {

            for (int j = 0; j <= n - 1; j++) {

                if (j == 0 || j == 4 || (i == j && i <= 2) || (i + j == 4 && i <= 2)) {

                    System.out.print("*");

                } else {

                    System.out.print(" ");

                }

            }

            System.out.println("");

        }

    }

}