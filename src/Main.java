
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static class Para {
        private int Waga;
        private int Wartosc;

        public int getWaga(){
            return this.Waga;
        }

        public int getWartosc(){
            return this.Wartosc;
        }

        public Para(){}

        public Para(int wartosc, int waga){
            this.Waga = waga;
            this.Wartosc = wartosc;
        }
    }
    public static void Plecak(Para[] tab, int n, int waga) throws FileNotFoundException {
        Para[][] tab_wynikowa = new Para[waga + 1][n + 1];
        //uzupełnianiae tablicy
        uzupelnianie(tab, n, waga, tab_wynikowa);

        //wypisanie
            for (int i = 0; i <= waga; i++) {
            for (int j = 0; j <= n; j++)
                System.out.print(tab_wynikowa[i][j].getWartosc() + "," + tab_wynikowa[i][j].getWaga() + " ");
            System.out.println();
        }

        LinkedList<LinkedList<Integer>> lista_odp = szukam_maksa(tab, n, waga, tab_wynikowa);
        zapis_do_pliku(lista_odp);
    }

    public static void uzupelnianie(Para[] tab, int n, int waga, Para[][] tab_wynikowa) {
        for (int i = 0; i <= waga; i++)
            for (int i1 = 0; i1 <= n; i1++) {
                if (i == 0 || i1 == 0)
                    tab_wynikowa[i][i1] = new Para(0, 0);
                else {
                    if (i < tab[i1 - 1].getWaga())
                        tab_wynikowa[i][i1] = new Para(tab_wynikowa[i][i1 - 1].getWartosc(), tab_wynikowa[i][i1 - 1].getWaga());
                    else {
                        if (tab_wynikowa[i][i1 - 1].getWartosc() > tab_wynikowa[i - tab[i1 - 1].getWaga()][i1 - 1].getWartosc() + tab[i1 - 1].getWartosc()) {
                            tab_wynikowa[i][i1] = new Para(tab_wynikowa[i][i1 - 1].getWartosc(), tab_wynikowa[i][i1 - 1].getWaga());
                        } else {
                            tab_wynikowa[i][i1] = new Para(tab_wynikowa[i - tab[i1 - 1].getWaga()][i1 - 1].getWartosc() + tab[i1 - 1].getWartosc(), tab_wynikowa[i - tab[i1 - 1].getWaga()][i1 - 1].getWaga() + tab[i1 - 1].getWaga());
                        }
                    }
                }
            }
    }

    public static void zapis_do_pliku(LinkedList<LinkedList<Integer>> lista_odp) throws FileNotFoundException {
        PrintWriter filewriter = new PrintWriter("Out0302.txt");
        for(int i = lista_odp.size(); i > 0; i--){
            for(int j = lista_odp.get(i-1).size(); j > 0; j--) {
                filewriter.print(lista_odp.get(i-1).get(j-1) + " ");
            }
            filewriter.println();
        }
        filewriter.close();
    }

    public static LinkedList<LinkedList<Integer>> szukam_maksa(Para[] tab, int n, int waga, Para[][] tab_wynikowa) {
        int max = 0;
        for (int i = 0; i <= waga; i++)
            for (int j = 0; j <= n; j++)
                if (max < tab_wynikowa[i][j].getWartosc())
                    max = tab_wynikowa[i][j].getWartosc();
        int tempmax = max;
        LinkedList<LinkedList<Integer>> lista_odp = new LinkedList<>();
        //szukam odpowiedzi i zapisuję ją do listy
        for(int i = waga; i >= 0; i--) {
            for (int j = n; j >= 0; j--) {
                LinkedList<Integer> kolejneliczby = new LinkedList<>();
                int x = i, z = j;
                if (tab_wynikowa[x][z].getWartosc() == tempmax) {
                    tempmax -= tab[z - 1].getWartosc();
                    x = x - tab[z - 1].getWaga();
                    z--;
                    kolejneliczby.add(z + 1);
                    while (tempmax != 0) {
                        if (tab_wynikowa[x][z].getWartosc() == tab_wynikowa[x][z - 1].getWartosc()) {
                            z--;
                        } else {
                            kolejneliczby.add(z);
                            tempmax -= tab[z - 1].getWartosc();
                            x = x - tab[z - 1].getWaga();
                            z--;
                        }
                    }
                }
                if (tempmax != max)
                    tempmax = max;
                if (kolejneliczby.size() != 0)
                    lista_odp.add(kolejneliczby);
            }
        }
        return lista_odp;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("In0302.txt");
        Scanner sc = new Scanner(file);

        int ile_p = sc.nextInt(), waga = sc.nextInt();
        Para[] tab = new Para[ile_p];

        for(int i = 0; sc.hasNext(); i++) {
            tab[i] = new Para(sc.nextInt(), sc.nextInt());
            System.out.println(tab[i].getWartosc() + "," + tab[i].getWaga());
        }

        Plecak(tab, ile_p, waga);
    }
}